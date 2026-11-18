package com.tecsup.barzola.centro_de_tareas.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.barzola.centro_de_tareas.data.TareasRepository
import com.tecsup.barzola.centro_de_tareas.model.CargaUi
import com.tecsup.barzola.centro_de_tareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    // Tarea con progreso: pasos 1 a 4.
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Demostración temporal del bloqueo; se reemplaza en el paso 2.
    fun tareaLargaMala() {
        _mensaje.value = "Trabajando..."
        Thread.sleep(4000)
        _mensaje.value = "Terminó"
    }
    // TODO Paso 2: sustituirla por fun tareaLarga() con corrutina y logs HILO.
    // TODO Paso 3: progreso como StateFlow<Float> de solo lectura.
    // TODO Paso 4: trabajo (Job?), corriendo y fun cancelar().

    // Contar primos: paso 5.
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    // TODO Paso 5: fun contar(hasta: Int) y fun cancelarPrimos().

    // Dos cargas: pasos 6 y 7.
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // TODO Paso 6: fun cargarUnaPorUna() y fun cargarALaVez().
    // TODO Paso 7: simularError, cambiarSimularError y manejo de errores.
}
