package com.tecsup.barzola.centro_de_tareas.data

interface TareasRepository {
    suspend fun contarPrimos(hasta: Int, onProgress: (Float) -> Unit): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
