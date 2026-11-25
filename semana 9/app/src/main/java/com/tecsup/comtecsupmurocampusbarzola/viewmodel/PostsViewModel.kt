package com.tecsup.comtecsupmurocampusbarzola.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.comtecsupmurocampusbarzola.data.PostRepository
import com.tecsup.comtecsupmurocampusbarzola.model.Comentario
import com.tecsup.comtecsupmurocampusbarzola.model.NuevoPost
import com.tecsup.comtecsupmurocampusbarzola.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()

    val lista: StateFlow<UiState<List<Post>>> = combine(_lista, _busqueda) { estado, texto ->
        if (estado is UiState.Exito && texto.isNotBlank()) {
            UiState.Exito(estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) })
        } else {
            estado
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Cargando)

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion: StateFlow<UiState<Post>?> = _publicacion.asStateFlow()

    private val idsEliminados = mutableSetOf<Int>()
    private val _eliminacion = MutableStateFlow<UiState<Int>?>(null)
    val eliminacion: StateFlow<UiState<Int>?> = _eliminacion.asStateFlow()

    init {
        cargar()
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun cargar() {
        filtrar(_usuarioFiltro.value)
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val result = if (userId == null) repo.listar() else repo.porUsuario(userId)
            result
                .onSuccess { posts ->
                    _lista.value = UiState.Exito(posts.filterNot { it.id in idsEliminados })
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun prepararPublicacion() {
        if (_publicacion.value != UiState.Cargando) {
            _publicacion.value = null
        }
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank() || _publicacion.value == UiState.Cargando) return

        _publicacion.value = UiState.Cargando
        viewModelScope.launch {
            val nuevo = NuevoPost(userId = 1, title = titulo, body = texto)
            _publicacion.value = repo.crear(nuevo).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun eliminar(id: Int) {
        if (_eliminacion.value == UiState.Cargando || id in idsEliminados) return

        _eliminacion.value = UiState.Cargando
        viewModelScope.launch {
            repo.eliminar(id)
                .onSuccess {
                    idsEliminados.add(id)
                    val listaActual = _lista.value
                    if (listaActual is UiState.Exito) {
                        _lista.value = UiState.Exito(listaActual.datos.filterNot { it.id == id })
                    }
                    _eliminacion.value = UiState.Exito(id)
                }
                .onFailure { _eliminacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun abrir(id: Int) {
        if (_eliminacion.value != UiState.Cargando) _eliminacion.value = null
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            coroutineScope {
                val postDeferred = async { repo.detalle(id) }
                val comentariosDeferred = async { repo.comentarios(id) }

                val postRes = postDeferred.await()
                val comentariosRes = comentariosDeferred.await()

                if (postRes.isSuccess && comentariosRes.isSuccess) {
                    _detalle.value = UiState.Exito(
                        DetalleUi(
                            post = postRes.getOrThrow(),
                            comentarios = comentariosRes.getOrThrow()
                        )
                    )
                } else {
                    val err = postRes.exceptionOrNull() ?: comentariosRes.exceptionOrNull() ?: Exception("Error desconocido")
                    _detalle.value = UiState.Error(mensajeDe(err))
                }
            }
        }
    }
}
