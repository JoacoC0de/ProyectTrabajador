package com.example.proyecttrabajador.data.model

data class Chat(
    val id: Int,
    val user: User,
    val client: User,
    val lastMessage: Message?
)