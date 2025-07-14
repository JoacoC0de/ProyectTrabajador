package com.tuapp.trabajador.data.repository

import com.example.proyecttrabajador.data.model.RegisterRequest
import com.example.proyecttrabajador.data.model.LoginRequest
import com.example.proyecttrabajador.data.network.ApiService
import okhttp3.MultipartBody

class TrabajadorRepository(private val api: ApiService) {
    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(request: RegisterRequest) =
        api.register(request)

    suspend fun getOccupations() = api.getOccupations()

    suspend fun setWorkerCategories(categories: List<Int>) =
        api.setWorkerCategories(categories)


    suspend fun uploadPhoto(filePart: MultipartBody.Part) =
        api.uploadPhoto(filePart)


}