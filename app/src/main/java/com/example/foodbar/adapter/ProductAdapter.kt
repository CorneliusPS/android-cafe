package com.example.foodbar.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbar.R
import com.example.foodbar.model.Product.Product
import java.text.NumberFormat
import java.util.Locale

import android.util.Base64

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.food_name)
        val textViewPrice: TextView = view.findViewById(R.id.food_price)
        val imageViewProduct: ImageView = view.findViewById(R.id.food_image)
        val textViewDescription: TextView = view.findViewById(R.id.food_desc)
        val textViewCategory: TextView = view.findViewById(R.id.food_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val hargaBarangFormat = NumberFormat.getNumberInstance(Locale("in", "ID")).format(product.hargaBarang)
        holder.textViewName.text = product.namaBarang
        holder.textViewPrice.text = "Rp. ${hargaBarangFormat}"

        holder.textViewDescription.text = product.deskripsiBarang
        holder.textViewCategory.text = "Category: ${product.kategoriBarang.namaKategori}"

        // Convert ByteArray to Bitmap and display it
        // Decode Base64 string to ByteArray and then Bitmap
        val imageBytes = Base64.decode(product.img, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imageViewProduct.setImageBitmap(decodedImage)
    }

    override fun getItemCount() = products.size
}