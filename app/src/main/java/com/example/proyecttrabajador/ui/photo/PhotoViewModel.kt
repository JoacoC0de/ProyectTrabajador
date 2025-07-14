// app/src/main/java/com/example/proyecttrabajador/ui/photo/PhotoViewModel.kt
package com.example.proyecttrabajador.ui.photo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tuapp.trabajador.data.repository.TrabajadorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class PhotoViewModel(private val repository: TrabajadorRepository) : ViewModel() {
    suspend fun uploadPhoto(filePart: MultipartBody.Part): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = repository.uploadPhoto(filePart)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PhotoViewModel", "Código: ${response.code()}, error: $errorBody")
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}