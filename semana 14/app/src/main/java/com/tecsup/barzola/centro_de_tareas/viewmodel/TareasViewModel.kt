package com.tecsup.barzola.centro_de_tareas.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun tareaLarga() {
        viewModelScope.launch {
            _mensaje.value = "Trabajando..."
            Log.d("HILO", "Antes: ${Thread.currentThread().name}")
            withContext(Dispatchers.Default) {
                Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                delay(4000)
            }
            Log.d("HILO", "Despues: ${Thread.currentThread().name}")
            _mensaje.value = "Terminó"
        }
    }
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
