package com.example.lab3_fitness_tracker_dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentSteps = 0
    private val stepGoal = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set today's date
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val sdf = SimpleDateFormat("EEEE, MMM d yyyy", Locale.getDefault())
        tvDate.text = sdf.format(Date())

        // Update Stats button
        val btnUpdate = findViewById<Button>(R.id.btn_update)
        btnUpdate.setOnClickListener { showUpdateDialog() }
    }

    private fun showUpdateDialog() {
        val input = EditText(this)
        input.hint = "Enter step count"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Update Steps")
            .setMessage("Enter your current step count:")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                val entered = input.text.toString().toIntOrNull()
                if (entered != null && entered >= 0) {
                    currentSteps = entered
                    updateUI()
                } else {
                    Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateUI() {
        // Update steps card
        findViewById<TextView>(R.id.tv_steps_count).text = currentSteps.toString()

        // Recalculate progress (cap at 100)
        val progress = ((currentSteps.toFloat() / stepGoal) * 100).toInt().coerceAtMost(100)

        // Update ProgressBar
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.progress = progress

        // Update percentage label
        findViewById<TextView>(R.id.tv_progress_percent).text = "$progress%"

        // Show motivational Toast at 100%
        if (progress >= 100) {
            Toast.makeText(
                this,
                "🎉 Goal reached! You're amazing!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
