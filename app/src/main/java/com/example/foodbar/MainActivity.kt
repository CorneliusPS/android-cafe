package com.example.foodbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.foodbar.databinding.ActivityMainBinding
import com.example.foodbar.view.LoginActivity
import com.example.foodbar.view.fragments.ListMenuFragment
import com.example.foodbar.view.fragments.MyReservationFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.nav_list_menu -> ListMenuFragment()
                R.id.nav_my_reservation -> MyReservationFragment()
                R.id.nav_logout -> {
                    logout()
                    null
                }
                else -> null
            }
            fragment?.let { replaceFragment(it, menuItem.title.toString()) }
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        if (savedInstanceState == null) {
            intent.getStringExtra("openFragment")?.let {
                if (it == "ListMenuFragment") {
                    replaceFragment(ListMenuFragment(), "List Menu")
                    binding.navView.setCheckedItem(R.id.nav_list_menu)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
        supportActionBar?.title = title
    }



    private fun logout() {
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE).edit().apply {
            remove("token")
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
