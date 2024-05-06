package com.example.foodbar.controller

import com.example.foodbar.model.Product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductController {
    @GET("api/customer/{idKategori}/products")
    suspend fun getProductsByCategory(
        @Header("Authorization") authToken: String,
        @Path("idKategori") idKategori: Long
    ): Response<ProductResponse>

    @GET("api/customer/products")
    suspend fun getAllProducts(
        @Header("Authorization") authToken: String,
    ): Response<ProductResponse>

}