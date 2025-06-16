package com.example.diplom

import com.example.diplom.VM.ExerciseViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ExerciseViewModelTest {

    @Test
    fun CheckDB(): Unit = runTest {
        // 1. Создаем ViewModel (в реальном проекте можно мокировать Firebase)
        val viewModel = ExerciseViewModel()

        // 2. Ждем загрузки данных (в реальном тесте используем `waitUntil` или моки)
        // Допустим, в Firebase 3 элемента
        val expectedCount = 3

        // 3. Проверяем, что ViewModel получила столько же
        when (val state = viewModel.uiState.value) {
            is ExerciseViewModel.ExerciseUiState.Success -> {
                Assert.assertEquals(expectedCount, state.exercises.size)
            }
            else -> throw AssertionError("Data not loaded!")
        }
    }
}