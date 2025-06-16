package com.example.diplom

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diplom.VM.ExerciseViewModel
import org.junit.Rule
import org.junit.Test

class ExerciseScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule() // Правило для тестов Compose

    @Test
    fun CeckBD() {
        // 1. Устанавливаем Composable с тестовыми данными
        composeTestRule.setContent {
            ExerciseScreenTest() // Передаем ViewModel
        }

        // 2. Проверяем количество элементов с тегом "ExerciseItem"
        composeTestRule.onAllNodesWithTag("ExerciseItem")
            .assertCountEquals(0)
    }


}
