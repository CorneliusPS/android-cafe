package com.example.foodbar.controller

import com.example.foodbar.model.Category.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoryController {
    @GET("api/customer/categories")
    suspend fun getCategories(
        @Header("Authorization") authToken: String,
    ): Response<CategoryResponse>
}