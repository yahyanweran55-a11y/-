package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_ai_items")
data class SavedAiItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val toolId: String,
    val toolTitle: String,
    val inputPrompt: String,
    val outputContent: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val tags: String = ""
)
