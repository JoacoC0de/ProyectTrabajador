package com.example.proyecttrabajador.data.network

import com.example.proyecttrabajador.data.model.Appointment
import com.example.proyecttrabajador.data.model.Chat
import com.example.proyecttrabajador.data.model.ProfilePictureRequest
import com.example.proyecttrabajador.data.model.RegisterRequest
import com.example.proyecttrabajador.data.model.LoginRequest
import com.example.proyecttrabajador.data.model.LoginResponse
import com.example.proyecttrabajador.data.model.Message
import com.example.proyecttrabajador.data.model.MessageRequest
import com.example.proyecttrabajador.data.model.Occupation
import com.example.proyecttrabajador.data.model.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("worker/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("worker/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("categories")
    suspend fun getOccupations(): Response<List<Occupation>>

    @POST("worker/categories")
    suspend fun setWorkerCategories(@Body categories: List<Int>): Response<Void>

    @Multipart
    @POST("workers/profile-picture")
    suspend fun uploadPhoto(
        @Part picture: MultipartBody.Part
    ): Response<Void>

    @GET("worker/chats")
    suspend fun getChats(): Response<List<Chat>>

    @GET("chats/{chatId}/messages")
    suspend fun getMessages(@Path("chatId") chatId: Int): Response<List<Message>>

    @POST("chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: Int,
        @Body message: MessageRequest
    ): Response<Message>

    @GET("appointments")
    suspend fun getAppointments(): Response<List<Appointment>>

    @GET("appointments/{appointmentId}/chats")
    suspend fun getAppointmentChats(@Path("appointmentId") appointmentId: Int): Response<List<Message>>
}