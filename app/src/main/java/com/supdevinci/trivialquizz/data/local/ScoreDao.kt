package com.supdevinci.trivialquizz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY percentage DESC, date DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>

    @Query("DELETE FROM scores")
    suspend fun clearScores()
}
