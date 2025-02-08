package com.example.calculateage

import android.graphics.Color
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
import java.time.Month
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

        // Get all the inputs, calendar and button.
        val firstNameInput = findViewById<EditText>(R.id.first_name_input)
        val lastNameInput = findViewById<EditText>(R.id.last_name_input)
        val birthDatePicker = findViewById<CalendarView>(R.id.birthdate_picker)
        val calcAgeButton = findViewById<Button>(R.id.calc_age_button)

        val today = Calendar.getInstance().timeInMillis

        // Sets the max date allowed to be picked to today's date.
        birthDatePicker.maxDate = today

        // Sets the initial value to today's date in milliseconds.
        var birthDateInMs = today

        // Set a new birthDateInMs when a new date in the calendar is selected.
        birthDatePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            birthDateInMs = calendar.timeInMillis
        }

        // When the button is clicked will greet the user with their age and name.
        calcAgeButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            if (firstName.isEmpty()) {
                toastError("First name is required!")
                return@setOnClickListener
            }

            val lastName = lastNameInput.text.toString().trim()
            if (lastName.isEmpty()) {
                toastError("Last name is required!")
                return@setOnClickListener
            }

            toastGreetingAndAge(firstName, lastName, calcAge(birthDateInMs))
        }
    }

    /**
     * Calculates the age based on the birthDatePicker input.
     * @param birthDateInMs The birth date in milliseconds since January 1, 1970.
     * @return The calculated age in years.
     */
    private fun calcAge(birthDateInMs: Long): Int {
        // Calendar instance that represents the date the user selected
        val birthDate = Calendar.getInstance().apply { timeInMillis = birthDateInMs }

        // Calendar instance that represents today's date.
        val today = Calendar.getInstance()

        // Set age of the user by subtracting today's year and the birth year.
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        // This is a check if the users current month is the same or later month.
        // OR
        // If the current month is the same as the birth month, did the current day of the month
        // less than the birth day.
        // If this either of these checks are true, reduce the age by one.
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                        today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--
        }

        return age
    }

    /**
     * Shows a toast to the user with a greeting and their age.
     * @param firstName First name of the user.
     * @param lastName Last name of the user.
     * @param age Age of the user.
     */
    private fun toastGreetingAndAge(firstName: String, lastName: String, age: Int) {
        // Create the text with the greeting and age.
        val greeting = "Hello $firstName $lastName!\nYou are $age years old today!"

        // Display the greeting and age.
        Toast.makeText(this, greeting, Toast.LENGTH_LONG).show()
    }

    /**
     * Shows a toast with an error message to the user when inputs are not valid.
     * @param message The message to show the user.
     */
    private fun toastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}