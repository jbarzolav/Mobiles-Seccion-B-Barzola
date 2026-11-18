package com.tecsup.barzola_campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tecsup.barzola_campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    context: Context
) : UbicacionRepository {

    private val cliente = LocationServices
        .getFusedLocationProviderClient(context.applicationContext)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return try {
            // Intentar primero obtener la última ubicación conocida
            var ubicacion = cliente.lastLocation.await()

            // Si es null, solicitar ubicación actual con prioridad equilibrada
            if (ubicacion == null) {
                ubicacion = cliente.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null
                ).await()
            }

            if (ubicacion != null) {
                Result.success(
                    Posicion(
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude
                    )
                )
            } else {
                Result.failure(
                    IllegalStateException("No se pudo obtener la ubicación. Verifica que el GPS esté activado en tu dispositivo.")
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
