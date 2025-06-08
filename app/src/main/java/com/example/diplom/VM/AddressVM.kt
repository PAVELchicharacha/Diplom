package com.example.diplom.VM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom.DATA.AddressData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressVM :ViewModel(){
    private val database = Firebase.database.reference

    // Состояния UI
    private val _uiState2 = MutableStateFlow<PlaceUiState>(PlaceUiState.Loading)
    val AddreessUiState: StateFlow<PlaceUiState> = _uiState2.asStateFlow()

    // Фильтры
    private val _currentFilter = MutableStateFlow(ExerciseFilter.ALL)
    val currentFilter: StateFlow<ExerciseFilter> = _currentFilter.asStateFlow()

    sealed class PlaceUiState {
        object Loading : PlaceUiState()
        data class Success(val exercises: List<AddressData>) : PlaceUiState()
        data class Error(val message: String) : PlaceUiState()
        object Empty : PlaceUiState()
    }

    enum class ExerciseFilter {
        ALL, BEGINNER, INTERMEDIATE, ADVANCED
    }

    init {
        loadExercises()
    }

    fun setFilter(filter: ExerciseFilter) {
        _currentFilter.value = filter
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _uiState2.value = PlaceUiState.Loading

            val exercisesRef = database.child("Addresses")
            val query = when (_currentFilter.value) {
                ExerciseFilter.BEGINNER -> exercisesRef.orderByChild("difficulty").equalTo("beginner")
                ExerciseFilter.INTERMEDIATE -> exercisesRef.orderByChild("difficulty").equalTo("intermediate")
                ExerciseFilter.ADVANCED -> exercisesRef.orderByChild("difficulty").equalTo("advanced")
                else -> exercisesRef
            }

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val exerciseList = mutableListOf<AddressData>()

                        snapshot.children.forEach { child ->
                            child.getValue(AddressData::class.java)?.let { exercise ->
                                exerciseList.add(exercise.copy(address = child.key ?: ""))
                            }
                        }

                        _uiState2.value = when {
                            exerciseList.isEmpty() -> PlaceUiState.Empty
                            else -> PlaceUiState.Success(exerciseList)
                        }
                    } catch (e: Exception) {
                        _uiState2.value =
                            PlaceUiState.Error("Ошибка загрузки данных: ${e.localizedMessage}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _uiState2.value = PlaceUiState.Error(error.message)
                }
            })
        }
    }

    fun refreshData() {
        loadExercises()
    }
}