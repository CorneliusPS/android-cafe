package com.example.foodbar.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodbar.model.Category.Category
import com.example.foodbar.view.fragments.ListMenuFragment

class CategoryPagerAdapter(fragment: FragmentActivity, private val categories: List<Category>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return ListMenuFragment.newInstance(categories[position].idKategori)
    }
}