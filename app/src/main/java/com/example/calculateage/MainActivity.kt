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
import java.text.SimpleDateFormat
import java.time.Month
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.microseconds

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
        val dateInput = findViewById<EditText>(R.id.date_input)
        val calcAgeButton = findViewById<Button>(R.id.calc_age_button)

        /**
         * When the button is clicked will greet the user with their age and name,
         * or display a toast with what part of the form is not filled out correctly.
         */
        calcAgeButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()

            // If the first name input was not filled in, send error to the user.
            if (firstName.isEmpty()) {
                toast("First name is required!")
                return@setOnClickListener
            }

            val lastName = lastNameInput.text.toString().trim()

            // If the last name input was not filled in, send error to the user.
            if (lastName.isEmpty()) {
                toast("Last name is required!")
                return@setOnClickListener
            }

            // If the date of birth input was not filled in, send error to the user.
            val birtDateStr = dateInput.text.toString().trim()
            if (birtDateStr.isEmpty()) {
                toast("Date of Birth is required!")
                return@setOnClickListener
            }

            // Parse the date of birth and return the time in milliseconds
            val birthDate = parseBirthDate(birtDateStr)

            // If the birth date is -1L for time in milliseconds, send the user the error message.
            if (birthDate == -1L) {
                toast("The Date of Birth entered is not valid, please use the format MM/DD/YYYY")
                return@setOnClickListener
            }

            // If the birth date is a future date, send the user a funny error message.
            if (isFutureDate(birthDate)) {
                toast("You must be a time traveler!\nPlease re-enter your Date of Birth!")
                return@setOnClickListener
            }

            // All error checks succeeded, send the user a greeting with their age.
            toast("Hello $firstName $lastName! You are ${calcAge(birthDate)} years old!")
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
     * Parses the string value of the date of birth into a time in milliseconds.
     * @param birthDateStr String value of the date
     * @return Date as time in milliseconds.  Invalid dates will return -1L.
     */
    private fun parseBirthDate(birthDateStr: String): Long {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US) // Correct format
        try {
            val birthDate = formatter.parse(birthDateStr)
            if (birthDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = birthDate
                return calendar.timeInMillis
            }
        } catch (e: Exception) {
            println("Caught exception parsing date from string: $e")
        }
        return -1L
    }

    /**
     * Determine if a date entered is in the future.
     * @param timeInMs The date represented as time in milliseconds.
     * @return true if the date is in the future
     */
    private fun isFutureDate(timeInMs: Long): Boolean {
        return timeInMs > Calendar.getInstance().timeInMillis
    }

    /**
     * Shows a toast with a message for the user.
     * @param message The message to show the user.
     */
    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }
}