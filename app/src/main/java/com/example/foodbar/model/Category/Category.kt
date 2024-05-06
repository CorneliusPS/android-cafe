package com.example.foodbar.model.Category

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("idKategori")
    val idKategori: Long,
    @SerializedName("namaKategori")
    val namaKategori: String
): Serializable
