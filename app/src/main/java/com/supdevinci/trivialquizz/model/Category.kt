package com.supdevinci.trivialquizz.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class CategoryResponse(
    @SerializedName("trivia_categories")
    val categories: List<Category>
)
