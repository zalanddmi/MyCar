package com.example.mycar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.mycar.R
import com.example.mycar.controllers.StatisticsController
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

        controller.getExpenses(carId, editTextDateFromStatistics.text.toString(), editTextDateToStatistics.text.toString()) { result ->
            textViewRefuelingDataStatistics.text = result.first
            textViewServiceDataStatistics.text = result.second
            textViewAllExpensesDataStatistics.text = result.third
        }

        controller.getMileage(carId, editTextDateFromStatistics.text.toString(), editTextDateToStatistics.text.toString()) { result ->
            textViewMileageDataStatistics.text = result
        }

        controller.getFuelMileage(carId, editTextDateFromStatistics.text.toString(), editTextDateToStatistics.text.toString()) { result ->
            textViewFuelMileageDataStatistics.text = result
        }

        controller.getRefuelingMileage(carId, editTextDateFromStatistics.text.toString(), editTextDateToStatistics.text.toString()) { result ->
            textViewRefuelingMileageDataStatistics.text = result
        }

        controller.getRefuelingDay(carId, editTextDateFromStatistics.text.toString(), editTextDateToStatistics.text.toString()) { result ->
            textViewRefuelingDayDataStatistics.text = result
        }
    }
}