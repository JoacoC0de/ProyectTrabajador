package com.example.proyecttrabajador.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val profile: Profile?
)