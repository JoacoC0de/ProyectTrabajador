package com.example.proyecttrabajador.data.model

data class Appointment(
    val id: Int,
    val client: User,
    val user: User?,
    val date: String?,
    val chat: Chat?
)