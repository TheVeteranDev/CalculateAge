package com.example.calculateage

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

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

        val firstNameInput = findViewById<EditText>(R.id.first_name_input)
        val lastNameInput = findViewById<EditText>(R.id.last_name_input)
        val birthDatePicker = findViewById<CalendarView>(R.id.birthdate_picker)
        val calcAgeButton = findViewById<Button>(R.id.calc_age_button)

        var birthDateInMillis = birthDatePicker.date

        birthDatePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            birthDateInMillis = calendar.timeInMillis
        }
        calcAgeButton.setOnClickListener {
            toastGreetingAndAge(
                firstNameInput.text.toString(),
                lastNameInput.text.toString(),
                calcAge(birthDateInMillis))
        }
    }

    /**
     *
     */
    private fun calcAge(birthDateInMillis: Long): Int {
        val birthDate = Calendar.getInstance().apply { timeInMillis = birthDateInMillis }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    private fun toastGreetingAndAge(firstName: String, lastName: String, age: Int) {
        val greeting = "Hello $firstName $lastName!\nYou are $age years old today!"
        Toast.makeText(this, greeting, Toast.LENGTH_LONG).show()
    }
}