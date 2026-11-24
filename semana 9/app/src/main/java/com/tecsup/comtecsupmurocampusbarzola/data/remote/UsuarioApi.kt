package com.tecsup.comtecsupmurocampusbarzola.data.remote

import com.tecsup.comtecsupmurocampusbarzola.model.Usuario
import retrofit2.http.GET

// Ejemplo [E]: interface UsuarioApi
interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}
