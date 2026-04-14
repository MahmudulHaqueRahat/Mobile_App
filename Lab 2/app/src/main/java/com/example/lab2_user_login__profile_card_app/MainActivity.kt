package com.example.lab2_user_login__profile_card_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // 1. Declare views using lateinit
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvForgot: TextView
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var profileCard: LinearLayout // Matches previous UI step
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Keeps the modern edge-to-edge look
        setContentView(R.layout.activity_main)

        // 2. Initialize views (Make sure these IDs match your activity_main.xml)
        etUsername   = findViewById(R.id.etUsername)
        etPassword   = findViewById(R.id.etPassword)
        tvForgot     = findViewById(R.id.tvForgotPassword)
        btnLogin     = findViewById(R.id.btnLogin)
        progressBar  = findViewById(R.id.progressBar)
        profileCard  = findViewById(R.id.layoutProfile)
        btnLogout    = findViewById(R.id.btnLogout)

        // 3. Set Click Listeners
        btnLogin.setOnClickListener { handleLogin() }
        btnLogout.setOnClickListener { handleLogout() }
        tvForgot.setOnClickListener { handleForgot() }

        // Fix for Edge-to-Edge padding (Optional but recommended)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleLogin() {
        val user = etUsername.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validation logic
        if (user == "admin" && pass == "1234") {
            // Show ProgressBar, hide login views
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false

            // Simulate network delay (1.5 seconds)
            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE

                // Hide Login Elements (Using IDs from previous step)
                findViewById<ImageView>(R.id.ivLogo).visibility = View.GONE
                findViewById<TextView>(R.id.tvWelcome).visibility = View.GONE
                etUsername.visibility = View.GONE
                etPassword.visibility = View.GONE
                tvForgot.visibility = View.GONE
                btnLogin.visibility = View.GONE

                // Show profile card
                profileCard.visibility = View.VISIBLE
            }, 1500)
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogout() {
        // Hide profile card
        profileCard.visibility = View.GONE

        // Show login form elements
        findViewById<ImageView>(R.id.ivLogo).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tvWelcome).visibility = View.VISIBLE
        etUsername.visibility = View.VISIBLE
        etPassword.visibility = View.VISIBLE
        tvForgot.visibility = View.VISIBLE
        btnLogin.visibility = View.VISIBLE
        btnLogin.isEnabled = true

        // Clear fields
        etUsername.text.clear()
        etPassword.text.clear()
    }

    private fun handleForgot() {
        Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_LONG).show()
    }
}