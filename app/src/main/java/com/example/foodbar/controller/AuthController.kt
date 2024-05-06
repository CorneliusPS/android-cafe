package com.example.foodbar.controller

import com.example.foodbar.model.Auth.LoginDTO
import com.example.foodbar.model.Auth.RegisDTO
import com.example.foodbar.model.Auth.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthController {
    @POST("api/auth0/login/v1")
    fun loginUser(@Body loginDTO: LoginDTO): Call<AuthResponse>

    @POST("api/auth0/v1/regis")
    fun registerUser(@Body regisDTO: RegisDTO): Call<AuthResponse>
}