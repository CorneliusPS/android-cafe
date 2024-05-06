package com.example.foodbar.model

data class SubError(
    val field: String,
    val message: String,
    val rejectedValue: Any
)
