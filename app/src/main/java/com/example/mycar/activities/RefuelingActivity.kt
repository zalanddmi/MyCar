package com.example.mycar.activities

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mycar.R
import com.example.mycar.controllers.RefuelingController
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RefuelingActivity : AppCompatActivity() {
    private lateinit var editTextDateRefueling: EditText
    private lateinit var spinnerTypeFuelRefueling: Spinner
    private lateinit var editTextSumRefueling: EditText
    private lateinit var editTextVolumeRefueling: EditText
    private lateinit var editTextMileageRefueling: EditText
    private lateinit var editTextStationRefueling: EditText
    private lateinit var buttonReadyRefueling: Button
    private lateinit var buttonDeleteRefueling: Button

    private lateinit var controller: RefuelingController

    private lateinit var date: String
    private lateinit var typeFuel: String
    private var sum: Double = 0.0
    private var volume: Double = 0.0
    private var mileage: Int = 0
    private lateinit var station: String
    private lateinit var carId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refueling)

        editTextDateRefueling = findViewById(R.id.editTextDateRefueling)
        spinnerTypeFuelRefueling = findViewById(R.id.spinnerTypeFuelRefueling)
        editTextSumRefueling = findViewById(R.id.editTextSumRefueling)
        editTextVolumeRefueling = findViewById(R.id.editTextVolumeRefueling)
        editTextMileageRefueling = findViewById(R.id.editTextMileageRefueling)
        editTextStationRefueling = findViewById(R.id.editTextStationRefueling)
        buttonReadyRefueling = findViewById(R.id.buttonReadyRefueling)
        buttonDeleteRefueling = findViewById(R.id.buttonDeleteRefueling)

        controller = RefuelingController()

        carId = intent.getStringExtra("carId")!!

        editTextDateRefueling.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val initialDate: Calendar
            val currentDateString = editTextDateRefueling.text.toString()
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

                    editTextDateRefueling.setText(formattedDate)
                },
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        val typeFuelList = mutableListOf("Выберете вид топлива", "АИ-100", "АИ-98", "АИ-95", "АИ-92", "ДТ",
            "КПГ", "ГАЗ")

        val typeFuelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeFuelList)
        typeFuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeFuelRefueling.adapter = typeFuelAdapter

        spinnerTypeFuelRefueling.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                typeFuel = if (position != 0) typeFuelList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        var expenseId = intent.getStringExtra("expenseId")
        if (expenseId != null) {
            buttonDeleteRefueling.visibility = View.VISIBLE
            controller.getService(expenseId) { result ->
                editTextDateRefueling.setText(result[0])
                val index = typeFuelList.indexOf(result[1])
                spinnerTypeFuelRefueling.setSelection(index)
                editTextSumRefueling.setText(result[2])
                editTextVolumeRefueling.setText(result[3])
                editTextMileageRefueling.setText(result[4])
                editTextStationRefueling.setText(result[5])
            }
        }

        buttonReadyRefueling.setOnClickListener {
            date = editTextDateRefueling.text.toString()
            if (date.isNullOrEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Выберете дату",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (typeFuel == "") {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Выберете вид топлива",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            sum = if (!editTextSumRefueling.text.isNullOrEmpty()) editTextSumRefueling.text.toString().toDouble() else 0.0
            if (sum == 0.0) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Введите сумму",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            volume = if (!editTextVolumeRefueling.text.isNullOrEmpty()) editTextVolumeRefueling.text.toString().toDouble() else 0.0
            if (volume == 0.0) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Введите кол-во залитого топлива",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            mileage = if (!editTextMileageRefueling.text.isNullOrEmpty()) editTextMileageRefueling.text.toString().toInt() else 0
            if (mileage == 0) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Введите пробег",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            station = if (!editTextStationRefueling.text.isNullOrEmpty()) editTextStationRefueling.text.toString() else ""
            if (expenseId != null) {
                controller.updateRefueling(expenseId, date, typeFuel, sum, volume, mileage, station, carId, this) { flag, text ->
                    if (!flag) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            text!!,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        finish()
                    }
                }
            }
            else {
                controller.addRefueling(date, typeFuel, sum, volume, mileage, station, carId) { flag, text ->
                    if (!flag) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            text!!,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        controller.navigateToDetails(carId, this)
                        finish()
                    }
                }
            }
        }

        buttonDeleteRefueling.setOnClickListener {
            controller.deleteRefueling(expenseId!!, carId, this)
            finish()
        }
    }
}