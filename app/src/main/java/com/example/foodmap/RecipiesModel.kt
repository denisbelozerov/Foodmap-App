package com.example.foodmap

data class RecipiesModel(
    val recipe: String,
    val type: String,
    val cusine: String,
    val safetyIndicator: Int,
    val favouriteRecipe: Int
)