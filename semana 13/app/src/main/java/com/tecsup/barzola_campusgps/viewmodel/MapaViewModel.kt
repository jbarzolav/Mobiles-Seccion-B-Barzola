package com.tecsup.barzola_campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.barzola_campusgps.data.LugaresRepository
import com.tecsup.barzola_campusgps.data.UbicacionRepository
import com.tecsup.barzola_campusgps.model.LugarConDistancia
import com.tecsup.barzola_campusgps.model.Posicion
import com.tecsup.barzola_campusgps.model.UbicacionUi
import com.tecsup.barzola_campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    fun buscar() {
        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    _estado.value = UbicacionUi.Lista(posicion)
                    actualizarDistancias(posicion)
                }
                .onFailure { error ->
                    _estado.value = UbicacionUi.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    private fun actualizarDistancias(pos: Posicion) {
        val listaConDistancia = lugaresRepo.obtenerLugares().map { lugar ->
            val metros = distanciaMetros(pos, lugar)
            LugarConDistancia(lugar, metros)
        }.sortedBy { it.metros }

        _lugares.value = listaConDistancia
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    // TODO Paso 6: fun seleccionar(id: Int?)

    // TODO Paso 7: val masCercano: StateFlow<Lugar?>

    private fun mensajeDe(error: Throwable): String =
        when (error) {
            is SecurityException -> "Falta el permiso de ubicación"
            else -> error.message ?: "No se pudo obtener la ubicación"
        }
}
