package com.example.foodbar.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbar.R
import com.example.foodbar.adapter.ProductAdapter
import com.example.foodbar.controller.RetrofitClient
import com.example.foodbar.model.Product.Product
import com.example.foodbar.utils.SharedPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListMenuFragment : Fragment() {
    private var categoryId: Long = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var products: List<Product> = listOf()

    companion object {
        fun newInstance(categoryId: Long): ListMenuFragment {
            val fragment = ListMenuFragment()
            val args = Bundle()
            args.putLong("category_id", categoryId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getLong("category_id") ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_menu, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadProducts()
        return view
    }

    private fun loadProducts() {
       // getAll Product

        CoroutineScope(Dispatchers.IO).launch {
            val token = SharedPreferences.getToken(requireContext())
            val response = RetrofitClient.productController.getAllProducts("Bearer $token")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    products = response.body()?.data ?: listOf()
                    adapter = ProductAdapter(products)
                    recyclerView.adapter = adapter
                } else {
                    Snackbar.make(requireView(), "Failed to get products", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}