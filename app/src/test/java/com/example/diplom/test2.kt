package com.example.diplom

import com.example.diplom.VM.AddressVM
import com.example.diplom.VM.ExerciseViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class test2 {
    @Test
    fun CheckDB2(): Unit = runTest {

        val viewModel = AddressVM()

        // 2. Ждем загрузки данных (в реальном тесте используем `waitUntil` или моки)
        // Допустим, в Firebase 3 элемента
        val expectedCount = 3

        // 3. Проверяем, что ViewModel получила столько же
        when (val state = viewModel.AddreessUiState.value) {
            is AddressVM.PlaceUiState.Success -> {
                Assert.assertEquals(expectedCount, state.exercises.size)
            }
            else -> throw AssertionError("Data not loaded!")
        }
    }
}