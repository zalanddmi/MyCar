package com.example.mycar.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.mycar.R
import com.example.mycar.controllers.StatisticsController
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsActivity : AppCompatActivity() {
    private lateinit var editTextDateFromStatistics: EditText
    private lateinit var editTextDateToStatistics: EditText
    private lateinit var textViewRefuelingDataStatistics: TextView
    private lateinit var textViewServiceDataStatistics: TextView
    private lateinit var textViewAllExpensesDataStatistics: TextView
    private lateinit var textViewMileageDataStatistics: TextView
    private lateinit var textViewFuelMileageDataStatistics: TextView
    private lateinit var textViewRefuelingMileageDataStatistics: TextView
    private lateinit var textViewRefuelingDayDataStatistics: TextView

    private lateinit var controller: StatisticsController

    private lateinit var carId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        editTextDateFromStatistics = findViewById(R.id.editTextDateFromStatistics)
        editTextDateToStatistics = findViewById(R.id.editTextDateToStatistics)
        textViewRefuelingDataStatistics = findViewById(R.id.textViewRefuelingDataStatistics)
        textViewServiceDataStatistics = findViewById(R.id.textViewServiceDataStatistics)
        textViewAllExpensesDataStatistics = findViewById(R.id.textViewAllExpensesDataStatistics)
        textViewMileageDataStatistics = findViewById(R.id.textViewMileageDataStatistics)
        textViewFuelMileageDataStatistics = findViewById(R.id.textViewFuelMileageDataStatistics)
        textViewRefuelingMileageDataStatistics = findViewById(R.id.textViewRefuelingMileageDataStatistics)
        textViewRefuelingDayDataStatistics = findViewById(R.id.textViewRefuelingDayDataStatistics)

        controller = StatisticsController()

        carId = intent.getStringExtra("carId")!!

        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        editTextDateToStatistics.setText(currentDate)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        editTextDateFromStatistics.setText(firstDayOfMonth)

        updateStatistics()

        editTextDateFromStatistics.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val initialDate: Calendar
            val currentDateString = editTextDateFromStatistics.text.toString()
            if (currentDateString.isNotEmpty()) {
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDateString)
                initialDate = Calendar.getInstance().apply {
                    time = date
                }
            } else {
                initialDate = currentDate
            }

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    editTextDateFromStatistics.setText(formattedDate)
                    updateStatistics()
                },
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        editTextDateToStatistics.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val initialDate: Calendar
            val currentDateString = editTextDateToStatistics.text.toString()
            if (currentDateString.isNotEmpty()) {
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDateString)
                initialDate = Calendar.getInstance().apply {
                    time = date
                }
            } else {
                initialDate = currentDate
            }

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    editTextDateToStatistics.setText(formattedDate)
                    updateStatistics()
                },
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }
    }

    private fun updateStatistics() {
        val dateFrom = editTextDateFromStatistics.text.toString()
        val dateTo = editTextDateToStatistics.text.toString()

        if (isDatesValid(dateFrom, dateTo)) {
            controller.getExpenses(carId, dateFrom, dateTo) { result ->
                textViewRefuelingDataStatistics.text = result.first
                textViewServiceDataStatistics.text = result.second
                textViewAllExpensesDataStatistics.text = result.third
            }

            controller.getMileage(carId, dateFrom, dateTo) { result ->
                textViewMileageDataStatistics.text = result
            }

            controller.getFuelMileage(carId, dateFrom, dateTo) { result ->
                textViewFuelMileageDataStatistics.text = result
            }

            controller.getRefuelingMileage(carId, dateFrom, dateTo) { result ->
                textViewRefuelingMileageDataStatistics.text = result
            }

            controller.getRefuelingDay(carId, dateFrom, dateTo) { result ->
                textViewRefuelingDayDataStatistics.text = result
            }
        }
        else {
            Snackbar.make(findViewById(android.R.id.content), "Дата начала не может быть позже даты окончения", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun isDatesValid(dateFrom: String, dateTo: String): Boolean {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val startDate = dateFormat.parse(dateFrom)
            val endDate = dateFormat.parse(dateTo)

            return !startDate.after(endDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}