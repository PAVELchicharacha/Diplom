package com.example.diplom.VM

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.diplom.DATA.Data
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ExerciseViewModel : ViewModel() {
    private val database = Firebase.database.reference

    // Состояния UI
    private val _uiState = MutableStateFlow<ExerciseUiState>(ExerciseUiState.Loading)
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    // Фильтры
    private val _currentFilter = MutableStateFlow(ExerciseFilter.ALL)
    val currentFilter: StateFlow<ExerciseFilter> = _currentFilter.asStateFlow()

    sealed class ExerciseUiState {
        object Loading : ExerciseUiState()
        data class Success(val exercises: List<Data>) : ExerciseUiState()
        data class Error(val message: String) : ExerciseUiState()
        object Empty : ExerciseUiState()
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
            _uiState.value = ExerciseUiState.Loading

            val exercisesRef = database.child("exercises")
            val query = when (_currentFilter.value) {
                ExerciseFilter.BEGINNER -> exercisesRef.orderByChild("difficulty").equalTo("beginner")
                ExerciseFilter.INTERMEDIATE -> exercisesRef.orderByChild("difficulty").equalTo("intermediate")
                ExerciseFilter.ADVANCED -> exercisesRef.orderByChild("difficulty").equalTo("advanced")
                else -> exercisesRef
            }

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val exerciseList = mutableListOf<Data>()

                        snapshot.children.forEach { child ->
                            child.getValue(Data::class.java)?.let { exercise ->
                                exerciseList.add(exercise.copy(id = child.key ?: ""))
                            }
                        }

                        _uiState.value = when {
                            exerciseList.isEmpty() -> ExerciseUiState.Empty
                            else -> ExerciseUiState.Success(exerciseList)
                        }
                    } catch (e: Exception) {
                        _uiState.value =
                            ExerciseUiState.Error("Ошибка загрузки данных: ${e.localizedMessage}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _uiState.value = ExerciseUiState.Error(error.message)
                }
            })
        }
    }

    fun refreshData() {
        loadExercises()
    }


}
