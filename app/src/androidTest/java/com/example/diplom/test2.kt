package com.example.diplom

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import org.junit.Rule
import org.junit.Test



class PlaceScreenTest {
    @get:Rule
    val composeTestRule2 = createComposeRule() // Правило для тестов Compose

    @Test
    fun CeckBD2() {
        // 1. Устанавливаем Composable с тестовыми данными
        composeTestRule2.setContent {
            PlaceScreenTest() // Передаем ViewModel
        }

        // 2. Проверяем количество элементов с тегом "ExerciseItem"
        composeTestRule2.onAllNodesWithTag("PlaceItem")
            .assertCountEquals(0)
    }
}