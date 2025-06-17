package com.supdevinci.trivialquizz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val player: String,
    val category: String,
    val difficulty: String,
    val score: String,
    val percentage: Int,
    val date: Date
)
