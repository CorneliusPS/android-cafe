package com.example.foodbar.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodbar.R
import com.example.foodbar.model.ErrorResponse
import com.example.foodbar.model.Auth.RegisDTO
import com.example.foodbar.controller.RetrofitClient
import com.example.foodbar.model.Auth.AuthResponse
import com.example.foodbar.utils.HideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        rootView = findViewById<View>(android.R.id.content)

        val emailEditText = findViewById<TextInputEditText>(R.id.email)
        val usernameEditText = findViewById<TextInputEditText>(R.id.username)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password)
        val nameEditText = findViewById<TextInputEditText>(R.id.nama)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backToLogin = findViewById<TextView>(R.id.backToLoginText)

        registerButton.setOnClickListener {
            HideKeyboard.hideKeyboard(this)
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                registerUser(email, username, password, name)
            } else {
                Snackbar.make(rootView, "All fields are required", Snackbar.LENGTH_LONG).show()
            }
        }

        backToLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(email: String, username: String, password: String, name: String) {
        val regisDTO = RegisDTO(email, username, password, name)
        RetrofitClient.authController.registerUser(regisDTO).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Snackbar.make(rootView, "Registrasi Berhasil", Snackbar.LENGTH_LONG).show()
                    Thread.sleep(1000)
                    navigateToLogin()
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("RetrofitError", "Call failed: " + t.message)
                Snackbar.make(rootView, "Network Error: ${t.message}", Snackbar.LENGTH_LONG).show()
            }

        })
    }

    private fun handleErrorResponse(response: Response<AuthResponse>) {
        val gson = Gson()
        val errorBody = response.errorBody()?.string() ?: gson.toJson(response.body()?.message)
        try {
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val specificError = errorResponse.subErrors?.find { it.field in listOf("username", "email", "password", "namaLengkap") }
            val errorMessage = specificError?.message ?: "Gagal Registrasi: Kesalahan tidak diketahui"
            Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG).show()
        } catch (e: JsonSyntaxException) {
            Snackbar.make(rootView, "Kesalahan format respons: ${e.localizedMessage}", Snackbar.LENGTH_LONG).show()
            println("Kesalahan format respons: ${e.localizedMessage}")
        } catch (e: Exception) {
            Snackbar.make(rootView, "Gagal Registrasi: Kesalahan saat memparsing pesan kesalahan", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
