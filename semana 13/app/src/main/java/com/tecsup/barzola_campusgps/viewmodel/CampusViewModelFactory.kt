package com.tecsup.barzola_campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.barzola_campusgps.data.LugaresRepository
import com.tecsup.barzola_campusgps.data.PreferenciasRepository
import com.tecsup.barzola_campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapaViewModel(
                ubicacionRepo = ubicacionRepo,
                lugaresRepo = lugaresRepo,
                preferenciasRepo = preferenciasRepo
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
