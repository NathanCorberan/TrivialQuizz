package com.supdevinci.trivialquizz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreEntry(
    val rank: Int,
    val player: String,
    val category: String,
    val difficulty: String,
    val score: String,
    val percentage: Int,
    val date: String
) : Parcelable
