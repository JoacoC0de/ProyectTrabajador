package com.example.proyecttrabajador.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyecttrabajador.data.model.RegisterRequest
import com.example.proyecttrabajador.data.model.RegisterResponse
import com.tuapp.trabajador.data.repository.TrabajadorRepository
import com.example.proyecttrabajador.data.network.TokenProvider
import com.tuapp.trabajador.util.SessionManager
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: TrabajadorRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(RegisterRequest(name, lastName, email, password))
                if (response.isSuccessful && response.body() != null) {
                    // Guardar el token recibido
                    val token = response.body()?.token
                    if (token != null) {
                        TokenProvider.token = token
                        android.util.Log.d("RegisterViewModel", "Token guardado: $token")

                    }
                    _registerResult.value = Result.success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error al registrar"
                    android.util.Log.e("RegisterViewModel", "Error: $errorMsg")
                    _registerResult.value = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}