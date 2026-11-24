package com.tecsup.comtecsupmurocampusbarzola.data

import com.tecsup.comtecsupmurocampusbarzola.data.remote.UsuarioApi
import com.tecsup.comtecsupmurocampusbarzola.model.Usuario
import kotlin.coroutines.cancellation.CancellationException

// Ejemplo [E]: interface y Repository de Usuario
interface UsuarioRepository {
    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
    override suspend fun listar(): Result<List<Usuario>> =
        try {
            Result.success(api.listar())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelación
        } catch (e: Exception) {
            Result.failure(e)
        }
}
