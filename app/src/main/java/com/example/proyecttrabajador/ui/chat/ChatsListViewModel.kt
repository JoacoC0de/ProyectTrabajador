package com.example.proyecttrabajador.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecttrabajador.data.model.Chat

import com.example.proyecttrabajador.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log

class ChatsListViewModel : ViewModel() {
    private val _chats = MutableLiveData<Result<List<Chat>>>()
    val chats: LiveData<Result<List<Chat>>> = _chats

    fun loadChats(currentUserId: Int) {
        viewModelScope.launch {
            try {
                val appointmentsResponse = RetrofitInstance.api.getAppointments()
                Log.d("ChatsViewModel", "Respuesta appointments código: ${appointmentsResponse.code()}")

                if (appointmentsResponse.isSuccessful) {
                    val appointments = appointmentsResponse.body() ?: emptyList()
                    Log.d("ChatsViewModel", "Appointments recibidos: ${appointments.size}")

                    // Crear chats virtuales basados en appointments
                    val chatsList = mutableListOf<Chat>()

                    for (appointment in appointments) {
                        try {
                            val messagesResponse = RetrofitInstance.api.getAppointmentChats(appointment.id)

                            if (messagesResponse.isSuccessful) {
                                val messages = messagesResponse.body() ?: emptyList()
                                Log.d("ChatsViewModel", "Appointment ${appointment.id}: ${messages.size} mensajes")

                                if (messages.isNotEmpty()) {
                                    val lastMessage = messages.lastOrNull()
                                    val chat = Chat(
                                        id = appointment.id, // Usar appointment ID como chat ID
                                        user = appointment.user ?: appointment.client, // Trabajador
                                        client = appointment.client,
                                        lastMessage = lastMessage
                                    )
                                    chatsList.add(chat)
                                    Log.d("ChatsViewModel", "Chat creado para appointment ${appointment.id}")
                                }
                            } else {
                                Log.w("ChatsViewModel", "No se pudieron obtener mensajes para appointment ${appointment.id}")
                            }
                        } catch (e: Exception) {
                            Log.w("ChatsViewModel", "Error al obtener mensajes para appointment ${appointment.id}: ${e.message}")
                        }
                    }

                    // Si no hay chats con mensajes, crear chats virtuales básicos
                    if (chatsList.isEmpty()) {
                        Log.d("ChatsViewModel", "No hay mensajes, creando chats virtuales básicos")

                        // Agrupar appointments por cliente para evitar duplicados
                        val uniqueClients = appointments.distinctBy { it.client.id }

                        chatsList.addAll(uniqueClients.map { appointment ->
                            Chat(
                                id = appointment.id,
                                user = appointment.user ?: appointment.client,
                                client = appointment.client,
                                lastMessage = null
                            )
                        })
                    }

                    Log.d("ChatsViewModel", "Chats finales: ${chatsList.size}")
                    _chats.value = Result.success(chatsList)
                } else {
                    Log.e("ChatsViewModel", "Error en appointments: ${appointmentsResponse.errorBody()?.string()}")
                    _chats.value = Result.failure(Exception("Error: ${appointmentsResponse.code()}"))
                }
            } catch (e: Exception) {
                Log.e("ChatsViewModel", "Excepción: ${e.message}")
                _chats.value = Result.failure(e)
            }
        }
    }
}