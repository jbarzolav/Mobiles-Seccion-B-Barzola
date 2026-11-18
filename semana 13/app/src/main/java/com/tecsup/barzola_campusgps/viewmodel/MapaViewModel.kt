package com.tecsup.barzola_campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.barzola_campusgps.data.LugaresRepository
import com.tecsup.barzola_campusgps.data.UbicacionRepository
import com.tecsup.barzola_campusgps.model.Lugar
import com.tecsup.barzola_campusgps.model.LugarConDistancia
import com.tecsup.barzola_campusgps.model.Posicion
import com.tecsup.barzola_campusgps.model.TipoError
import com.tecsup.barzola_campusgps.model.UbicacionUi
import com.tecsup.barzola_campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { lugar: Lugar ->
            LugarConDistancia(lugar, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    val masCercano: StateFlow<Lugar?> = flow {
        lugares.collect { lista ->
            emit(lista.firstOrNull { it.metros != null }?.lugar)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services.",
                tipo = TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    _estado.value = UbicacionUi.Lista(posicion)
                    actualizarDistancias(posicion)
                }
                .onFailure { error ->
                    val tipo = if (error is IllegalStateException || error.message?.contains("GPS") == true) {
                        TipoError.GPS_APAGADO
                    } else {
                        TipoError.OTRO
                    }
                    _estado.value = UbicacionUi.Error(
                        mensaje = error.message ?: "No se pudo obtener la ubicación",
                        tipo = tipo
                    )
                }
        }
    }

    private fun actualizarDistancias(pos: Posicion) {
        val listaConDistancia = lugaresRepo.obtenerLugares().map { lugar: Lugar ->
            val metros = distanciaMetros(pos, lugar)
            LugarConDistancia(lugar, metros)
        }.sortedBy { it.metros }

        _lugares.value = listaConDistancia
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }
}
