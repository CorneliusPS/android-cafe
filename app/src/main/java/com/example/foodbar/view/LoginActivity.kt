package com.example.foodbar.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodbar.R
import com.example.foodbar.model.Auth.LoginDTO
import com.example.foodbar.controller.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.TextView
import com.example.foodbar.MainActivity
import com.example.foodbar.model.Auth.AuthResponse
import com.example.foodbar.model.ErrorResponse
import com.example.foodbar.utils.HideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        rootView = findViewById<View>(android.R.id.content)

        val usernameEditText = findViewById<TextInputEditText>(R.id.username)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerLinkTextView = findViewById<TextView>(R.id.registerLink)

        loginButton.setOnClickListener {
            HideKeyboard.hideKeyboard(this)
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Snackbar.make(rootView, "Please enter both username and password", Snackbar.LENGTH_LONG).show()
            }
        }

        registerLinkTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        val loginDTO = LoginDTO(username, password)
        RetrofitClient.authController.loginUser(loginDTO).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseData = response.body()?.data
                    if (responseData is Map<*, *>) {
                        val token = responseData["token"] as? String
                        val id = responseData["userId"] as? Double

                        if (token != null && id != null) {
                            saveUser(token, id)
                            navigateToHomePage()
                            Thread.sleep(1000)
                            Snackbar.make(rootView, "Welcome Buddy!", Snackbar.LENGTH_LONG).show()
                        }
                        printSharedPreferences()
                    } else {
                        Snackbar.make(rootView, "Failed to retrieve the authentication token.", Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Snackbar.make(rootView, "Network Error: ${t.message}", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun saveUser(token: String, id: Double) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token", token)
            putLong("userId", id.toLong())
            apply()
        }
    }

    fun printSharedPreferences() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val allEntries = prefs.all
        for (entry in allEntries.entries) {
            Log.d("SharedPreferences", "${entry.key}: ${entry.value}")
        }
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("openFragment", "ListMenuFragment")
        startActivity(intent)
        finish()
    }

    private fun handleErrorResponse(response: Response<AuthResponse>) {
        val gson = Gson()
        val errorBody = response.errorBody()?.string() ?: gson.toJson(response.body()?.message)
        try {
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val errorMessage = errorResponse?.message ?: "Gagal Login: Kesalahan tidak diketahui"
            Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG).show()
        } catch (e: JsonSyntaxException) {
            Snackbar.make(rootView, "Kesalahan format respons: ${e.localizedMessage}", Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            Snackbar.make(rootView, "Gagal Login: Kesalahan saat memparsing pesan kesalahan", Snackbar.LENGTH_LONG).show()
        }
    }

}