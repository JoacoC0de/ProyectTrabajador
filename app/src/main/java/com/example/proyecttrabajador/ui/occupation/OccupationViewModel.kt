// app/src/main/java/com/example/proyecttrabajador/ui/occupation/OccupationViewModel.kt
package com.example.proyecttrabajador.ui.occupation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecttrabajador.data.model.Occupation
import com.tuapp.trabajador.data.repository.TrabajadorRepository
import kotlinx.coroutines.launch

class OccupationViewModel(private val repository: TrabajadorRepository) : ViewModel() {
    private val _occupations = MutableLiveData<List<Occupation>>()
    val occupations: LiveData<List<Occupation>> = _occupations

    private val _selected = MutableLiveData<Set<Int>>(setOf())
    val selected: LiveData<Set<Int>> = _selected

    fun fetchOccupations() {
        viewModelScope.launch {
            val response = repository.getOccupations()
            if (response.isSuccessful && response.body() != null) {
                _occupations.value = response.body()
                Log.d("OccupationViewModel", "Ocupaciones recibidas: ${response.body()?.size ?: 0}")            } else {
                Log.e("OccupationViewModel", "Error al obtener ocupaciones")            }
        }
    }

    fun toggleOccupation(id: Int) {
        val current = _selected.value?.toMutableSet() ?: mutableSetOf()
        if (current.contains(id)) current.remove(id) else current.add(id)
        _selected.value = current
    }
    private val _saveResult = MutableLiveData<Result<Unit>>()
    val saveResult: LiveData<Result<Unit>> = _saveResult

    fun saveOccupations(selectedIds: List<Int>) {
        viewModelScope.launch {
            try {
                val response = repository.setWorkerCategories(selectedIds)
                if (response.isSuccessful) {
                    _saveResult.value = Result.success(Unit)
                } else {
                    _saveResult.value = Result.failure(Exception("Error al guardar ocupaciones"))
                }
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }

}