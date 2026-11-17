package com.tecsup.barzola_campusgps.data

import com.tecsup.barzola_campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}
