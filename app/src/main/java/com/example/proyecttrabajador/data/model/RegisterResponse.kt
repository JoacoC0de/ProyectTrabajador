package com.example.proyecttrabajador.data.model

data class RegisterResponse(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val token: String?
)