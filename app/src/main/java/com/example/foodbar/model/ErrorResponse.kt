package com.example.foodbar.model

data class ErrorResponse(
    val serverResponse: String,
    val status: Int,
    val message: String,
    val path: String,
    val subErrors: List<SubError>,
    val errorCode: String
)
