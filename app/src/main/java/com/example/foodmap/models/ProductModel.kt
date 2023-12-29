package com.example.foodmap.models

data class ProductModel(
    val product: String,
    val category: String,
    val safetyIndicator: Int,
    val favouriteProduct: Int
)