package com.example.lab4_grade_report_app

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var totalSubjects = 0
    private var totalPassed = 0
    private var totalFailed = 0
    private var totalPoints = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val dynamicTable = findViewById<TableLayout>(R.id.dynamicRows)
        val etSubName = findViewById<EditText>(R.id.etSubName)
        val etObtained = findViewById<EditText>(R.id.etObtained)
        val etTotal = findViewById<EditText>(R.id.etTotal)

        btnAdd.setOnClickListener {
            val name = etSubName.text.toString()
            val obt = etObtained.text.toString().toIntOrNull()
            val tot = etTotal.text.toString().toIntOrNull()

            if (name.isNotEmpty() && obt != null && tot != null) {
                addGradeRow(name, obt, tot, dynamicTable)
                updateSummary()
                // Clear inputs
                etSubName.text.clear()
                etObtained.text.clear()
                etTotal.text.clear()
            }
            else {

                Toast.makeText(this, "Please enter a subject name and valid marks", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addGradeRow(sub: String, obt: Int, tot: Int, table: TableLayout) {
        val percentage = (obt.toDouble() / tot.toDouble()) * 100
        val grade = calculateGrade(percentage)
        val points = getGpaPoints(grade)

        val row = TableRow(this)
        row.setPadding(8, 8, 8, 8)

        // Alternating row background
        if (totalSubjects % 2 == 0) row.setBackgroundResource(R.color.row_even)

        val tvSub = TextView(this).apply { text = sub }
        val tvObt = TextView(this).apply { text = obt.toString() }
        val tvTot = TextView(this).apply { text = tot.toString() }
        val tvGrade = TextView(this).apply {
            text = grade
            // Highlight Pass/Fail
            setTextColor(if (grade == "F") Color.RED else Color.parseColor("#4CAF50"))
        }

        row.addView(tvSub)
        row.addView(tvObt)
        row.addView(tvTot)
        row.addView(tvGrade)
        table.addView(row)

        // Update Statistics
        totalSubjects++
        if (grade == "F") totalFailed++ else totalPassed++
        totalPoints += points
    }

    private fun calculateGrade(perc: Double): String {
        return when {
            perc >= 90 -> "A+"
            perc >= 80 -> "A"
            perc >= 70 -> "B+"
            perc >= 60 -> "B"
            perc >= 50 -> "C"
            perc >= 40 -> "D"
            else -> "F"
        }
    }

    private fun getGpaPoints(grade: String): Double {
        return when (grade) {
            "A+" -> 4.0; "A" -> 3.7; "B+" -> 3.3; "B" -> 3.0; "C" -> 2.0; "D" -> 1.0; else -> 0.0
        }
    }

    private fun updateSummary() {
        val avgGpa = if (totalSubjects > 0) totalPoints / totalSubjects else 0.0
        findViewById<TextView>(R.id.tvSummary).text = "Total: $totalSubjects | Passed: $totalPassed | Failed: $totalFailed"
        findViewById<TextView>(R.id.tvGPA).text = "GPA: %.2f".format(avgGpa)
    }
}