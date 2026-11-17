package com.tecsup.barzola_campusgps.data

import com.tecsup.barzola_campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
