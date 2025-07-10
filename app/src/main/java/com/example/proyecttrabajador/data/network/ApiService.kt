package com.example.proyecttrabajador.data.network

import com.example.proyecttrabajador.data.model.RegisterRequest
import com.example.proyecttrabajador.data.model.LoginRequest
import com.example.proyecttrabajador.data.model.LoginResponse
import com.example.proyecttrabajador.data.model.Occupation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.proyecttrabajador.data.model.RegisterResponse
import retrofit2.http.GET

interface ApiService {
    @POST("worker/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @POST("worker/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>


    @GET("categories")
    suspend fun getOccupations(): Response<List<Occupation>>

    @POST("worker/categories")
    suspend fun setWorkerCategories(@Body categories: List<Int>): Response<Void>
}