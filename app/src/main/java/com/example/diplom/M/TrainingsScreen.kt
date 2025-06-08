package com.example.diplom.M

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.diplom.DATA.Data
import com.example.diplom.VM.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrainingScreen(
    viewModel: ExerciseViewModel = viewModel(),
    onExerciseClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Упражнения") },
                actions = {
                    FilterMenu(currentFilter = currentFilter, onFilterSelected = viewModel::setFilter)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.refreshData() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Обновить")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is ExerciseViewModel.ExerciseUiState.Loading -> LoadingView()
                is ExerciseViewModel.ExerciseUiState.Success -> ExerciseList(
                    exercises = state.exercises,
                    onExerciseClick = onExerciseClick
                )
                is ExerciseViewModel.ExerciseUiState.Error -> ErrorView(message = state.message)
                ExerciseViewModel.ExerciseUiState.Empty -> EmptyView()
            }
        }
    }
}

@Composable
private fun FilterMenu(
    currentFilter: ExerciseViewModel.ExerciseFilter,
    onFilterSelected: (ExerciseViewModel.ExerciseFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.Build, contentDescription = "Фильтры")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        ExerciseViewModel.ExerciseFilter.values().forEach { filter ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = when (filter) {
                            ExerciseViewModel.ExerciseFilter.ALL -> "Все"
                            ExerciseViewModel.ExerciseFilter.BEGINNER -> "Начинающий"
                            ExerciseViewModel.ExerciseFilter.INTERMEDIATE -> "Средний"
                            ExerciseViewModel.ExerciseFilter.ADVANCED -> "Продвинутый"
                        }
                    )
                },
                onClick = {
                    onFilterSelected(filter)
                    expanded = false
                },
                leadingIcon = {
                    if (filter == currentFilter) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Ошибка",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun EmptyView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Нет данных",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Упражнения не найдены",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun ExerciseList(
    exercises: List<Data>,
    onExerciseClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(exercises, key = { it.id }) { exercise ->
            ExerciseCard(exercise = exercise, onClick = { onExerciseClick(exercise.id) })
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Data,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = exercise.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                    contentDescription = "Изображение упражнения ${exercise.name}",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = exercise.muscleGroup,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        DifficultyChip(difficulty = exercise.difficulty)
                        Spacer(modifier = Modifier.width(8.dp))
                        EquipmentChip(equipment = exercise.equipment)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExerciseStats(exercise = exercise)
        }
    }
}

@Composable
private fun DifficultyChip(difficulty: String) {
    val (text, color) = when (difficulty.lowercase()) {
        "beginner" -> "Начинающий" to Color(0xFF4CAF50)
        "intermediate" -> "Средний" to Color(0xFF2196F3)
        "advanced" -> "Продвинутый" to Color(0xFFF44336)
        else -> difficulty to MaterialTheme.colorScheme.primary
    }

    AssistChip(
        onClick = {},
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.2f),
            labelColor = color
        )
    )
}

@Composable
private fun EquipmentChip(equipment: String) {
    AssistChip(
        onClick = {},
        label = { Text(equipment) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}

@Composable
private fun ExerciseStats(exercise: Data) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatItem(
            icon = Icons.Default.Check,
            value = "${exercise.durationMinutes} мин",
            label = "Длительность"
        )

        StatItem(
            icon = Icons.Default.Check,
            value = "${exercise.caloriesBurned} ккал",
            label = "Калории"
        )

        if (exercise.videoUrl.isNotEmpty()) {
            StatItem(
                icon = Icons.Default.Check,
                value = "Видео",
                label = "Демонстрация"
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}