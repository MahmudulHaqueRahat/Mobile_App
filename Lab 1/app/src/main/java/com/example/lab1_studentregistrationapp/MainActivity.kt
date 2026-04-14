package com.example.lab1_studentregistrationapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//    Spinner
        val spinner = findViewById<Spinner>(R.id.spinnerCountry)

        val countries = arrayOf("Bangladesh", "India", "USA", "UK", "Canada")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter


//    Date time
        val btnDate = findViewById<Button>(R.id.btnDate)
        var selectedDate = ""

        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(this,
                { _, year, month, day ->
                    selectedDate = "$day/${month + 1}/$year"
                    btnDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
//    Submit
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {

            val id = findViewById<EditText>(R.id.etStudentId).text.toString()
            val name = findViewById<EditText>(R.id.etFullName).text.toString()
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            val age = findViewById<EditText>(R.id.etAge).text.toString()

            val radioGroup = findViewById<RadioGroup>(R.id.rgGender)
            val genderId = radioGroup.checkedRadioButtonId

            val spinner = findViewById<Spinner>(R.id.spinnerCountry)
            val country = spinner.selectedItem.toString()

            val sports = mutableListOf<String>()
            if (findViewById<CheckBox>(R.id.cbFootball).isChecked) sports.add("Football")
            if (findViewById<CheckBox>(R.id.cbCricket).isChecked) sports.add("Cricket")
            if (findViewById<CheckBox>(R.id.cbBasketball).isChecked) sports.add("Basketball")
            if (findViewById<CheckBox>(R.id.cbBadminton).isChecked) sports.add("Badminton")


            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter Student ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter Full Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter Email Address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.contains("@")) {
                Toast.makeText(this, "Please enter a valid Email Address (must contain @)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age.isEmpty()) {
                Toast.makeText(this, "Please enter Age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age.toIntOrNull() == null) {
                Toast.makeText(this, "Please enter a valid Age (numbers only)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age.toInt() <= 0) {
                Toast.makeText(this, "Age must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (genderId == -1) {
                Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<RadioButton>(genderId).text.toString()

            val message = """
        ID: $id
        Name: $name
        Gender: $gender
        Sports: ${sports.joinToString()}
        Country: $country
        DOB: $selectedDate
    """.trimIndent()

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()


        }


//        Reset
        val btnReset = findViewById<Button>(R.id.btnReset)

        btnReset.setOnClickListener {

            findViewById<EditText>(R.id.etStudentId).text.clear()
            findViewById<EditText>(R.id.etFullName).text.clear()
            findViewById<EditText>(R.id.etEmail).text.clear()
            findViewById<EditText>(R.id.etPassword).text.clear()
            findViewById<EditText>(R.id.etAge).text.clear()

            findViewById<RadioGroup>(R.id.rgGender).clearCheck()

            findViewById<CheckBox>(R.id.cbFootball).isChecked = false
            findViewById<CheckBox>(R.id.cbCricket).isChecked = false
            findViewById<CheckBox>(R.id.cbBasketball).isChecked = false
            findViewById<CheckBox>(R.id.cbBadminton).isChecked = false

            findViewById<Spinner>(R.id.spinnerCountry).setSelection(0)

            selectedDate = ""
            findViewById<Button>(R.id.btnDate).text = "Select Date"
        }

    }




}