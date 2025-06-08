package com.example.diplom.DATA
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Data(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val muscleGroup: String = "",
    val difficulty: String = "",
    val equipment: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val caloriesBurned: Int = 0,
    val durationMinutes: Int = 0,
    val createdAt: Long = 0
)
{
    constructor() : this("", "", "", "", "", "", "", "", 0, 0, 0)
}