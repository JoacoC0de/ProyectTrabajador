package com.example.proyecttrabajador.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecttrabajador.data.network.RetrofitInstance
import com.tuapp.trabajador.data.repository.TrabajadorRepository

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = TrabajadorRepository(RetrofitInstance.api)
        return LoginViewModel(repository) as T
    }
}