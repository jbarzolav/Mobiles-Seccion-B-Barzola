package com.tecsup.barzola_campusgps.util

import android.location.Location
import com.tecsup.barzola_campusgps.model.Lugar
import com.tecsup.barzola_campusgps.model.Posicion

fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultados = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultados
    )
    return resultados[0]
}
