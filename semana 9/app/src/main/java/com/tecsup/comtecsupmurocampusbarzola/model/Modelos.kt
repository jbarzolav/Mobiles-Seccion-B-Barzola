package com.tecsup.comtecsupmurocampusbarzola.model

import kotlinx.serialization.Serializable

// Ejemplo [E]: modelo de Usuario
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)
