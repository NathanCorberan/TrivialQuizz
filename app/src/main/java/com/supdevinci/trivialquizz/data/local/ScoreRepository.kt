package com.supdevinci.trivialquizz.data.local

import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val dao: ScoreDao) {
    val allScores: Flow<List<ScoreEntity>> = dao.getAllScores()

    suspend fun insert(score: ScoreEntity) = dao.insert(score)

    suspend fun clear() = dao.clearScores()
}