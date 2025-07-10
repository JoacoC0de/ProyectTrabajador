
package com.example.proyecttrabajador.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecttrabajador.data.network.RetrofitInstance
import com.tuapp.trabajador.data.repository.TrabajadorRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TrabajadorRepository) : ViewModel() {
    val loginResult = MutableLiveData<Result<Unit>>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                val token = response.body()?.access_token
                if (response.isSuccessful && token != null) {
                    com.example.proyecttrabajador.data.network.TokenProvider.token = token
                    loginResult.value = Result.success(Unit)
                } else {
                    loginResult.value = Result.failure(Exception("Credenciales incorrectas"))
                }
            } catch (e: Exception) {
                loginResult.value = Result.failure(e)
            }
        }
    }
}