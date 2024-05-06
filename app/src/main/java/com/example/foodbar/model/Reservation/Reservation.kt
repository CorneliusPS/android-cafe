package com.example.foodbar.model.Reservation

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class Reservation(
    @SerializedName("tableType")
    val tableType: String,
    @SerializedName("reservationDate")
    val reservationDate: Date, // Changed from Date to String
    @SerializedName("deskripsi")
    val deskripsi: String
): Serializable
