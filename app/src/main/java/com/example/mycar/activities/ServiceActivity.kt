package com.example.mycar.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.mycar.R
import com.example.mycar.controllers.ServiceController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ServiceActivity : AppCompatActivity() {
    private lateinit var editTextDateService: EditText
    private lateinit var spinnerTypeService: Spinner
    private lateinit var editTextSumService: EditText
    private lateinit var editTextMileageService: EditText
    private lateinit var buttonReadyService: Button

    private lateinit var controller: ServiceController

    private lateinit var date: String
    private lateinit var type: String
    private var sum: Double = 0.0
    private var mileage: Int = 0
    private lateinit var carId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        editTextDateService = findViewById(R.id.editTextDateService)
        spinnerTypeService = findViewById(R.id.spinnerTypeService)
        editTextSumService = findViewById(R.id.editTextSumService)
        editTextMileageService = findViewById(R.id.editTextMileageService)
        buttonReadyService = findViewById(R.id.buttonReadyService)

        controller = ServiceController()

        carId = intent.getStringExtra("carId")!!

        editTextDateService.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    editTextDateService.setText(formattedDate)
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        val typeFuelList = mutableListOf("Выберете услугу", "Запчасти", "КАСКО", "Мойка", "Налог", "ОСАГО",
            "Парковка", "Расходники", "Техобслуживание", "Шиномонтаж", "Штрафы", "Эвакуатор", "Другое")

        val typeFuelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeFuelList)
        typeFuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeService.adapter = typeFuelAdapter

        spinnerTypeService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                type = if (position != 0) typeFuelList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        buttonReadyService.setOnClickListener {
            date = editTextDateService.text.toString()
            sum = if (!editTextSumService.text.isNullOrEmpty()) editTextSumService.text.toString().toDouble() else 0.0
            mileage = if (!editTextMileageService.text.isNullOrEmpty()) editTextMileageService.text.toString().toInt() else 0
            controller.addService(date, type, sum, mileage, carId)
            finish()
        }
    }
}