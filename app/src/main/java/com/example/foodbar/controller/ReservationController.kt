package com.example.foodbar.controller


import com.example.foodbar.model.Reservation.Reservation
import com.example.foodbar.model.Reservation.ReservationResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReservationController {
    @POST("api/customer/reservation")
    suspend fun createReservation(
        @Header("Authorization") authToken: String,
        @Body reservation: Reservation
    ): Response<ReservationResponse>
}