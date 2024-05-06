package com.example.foodbar.model.Product

import com.example.foodbar.model.Category.Category
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Product(
    @SerializedName("idBarang")
    val idBarang: Int,
    @SerializedName("namaBarang")
    val namaBarang: String,
    @SerializedName("hargaBarang")
    val hargaBarang: Int,
    @SerializedName("deskripsiBarang")
    val deskripsiBarang: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("kategoriBarang")
    val kategoriBarang : Category
): Serializable
