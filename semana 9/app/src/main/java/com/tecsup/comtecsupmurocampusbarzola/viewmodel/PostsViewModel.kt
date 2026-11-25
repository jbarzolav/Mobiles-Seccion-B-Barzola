package com.tecsup.comtecsupmurocampusbarzola.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.comtecsupmurocampusbarzola.data.PostRepository
import com.tecsup.comtecsupmurocampusbarzola.model.Comentario
import com.tecsup.comtecsupmurocampusbarzola.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    init {
        cargar()
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
                    _lista.value = UiState.Exito(posts)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun abrir(id: Int) {
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
