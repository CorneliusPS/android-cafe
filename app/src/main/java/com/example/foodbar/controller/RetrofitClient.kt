package com.example.foodbar.controller


import com.example.foodbar.util.GsonUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://192.168.158.178:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonUtil.gson))
        .client(client)
        .build()


    val authController: AuthController by lazy {
        retrofit.create(AuthController::class.java)
    }

    val categoryController: CategoryController by lazy {
        retrofit.create(CategoryController::class.java)
    }

    val productController: ProductController by lazy {
        retrofit.create(ProductController::class.java)
    }

    val reservationController: ReservationController by lazy {
        retrofit.create(ReservationController::class.java)
    }

}