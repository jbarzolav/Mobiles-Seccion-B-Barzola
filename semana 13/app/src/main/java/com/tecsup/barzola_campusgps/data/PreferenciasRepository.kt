package com.tecsup.barzola_campusgps.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_campus")

class PreferenciasRepository(
    private val context: Context
) {
    companion object {
        private val ULTIMO_LUGAR_KEY = intPreferencesKey("ultimo_lugar_id")
    }

    val ultimoLugarId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[ULTIMO_LUGAR_KEY]
        }

    suspend fun guardarUltimoLugar(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[ULTIMO_LUGAR_KEY] = id
        }
    }
}
