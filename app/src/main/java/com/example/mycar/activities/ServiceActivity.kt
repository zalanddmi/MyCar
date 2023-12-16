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
    private lateinit var buttonDeleteService: Button

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
        buttonDeleteService = findViewById(R.id.buttonDeleteService)

        controller = ServiceController()

        carId = intent.getStringExtra("carId")!!

        editTextDateService.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val initialDate: Calendar
            val currentDateString = editTextDateService.text.toString()
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

                    editTextDateService.setText(formattedDate)
                },
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        val typeList = mutableListOf("Выберете услугу", "Запчасти", "КАСКО", "Мойка", "Налог", "ОСАГО",
            "Парковка", "Расходники", "Техобслуживание", "Шиномонтаж", "Штрафы", "Эвакуатор", "Другое")

        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeList)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeService.adapter = typeAdapter

        spinnerTypeService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                type = if (position != 0) typeList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        var expenseId = intent.getStringExtra("expenseId")
        if (expenseId != null) {
            buttonDeleteService.visibility = View.VISIBLE
            controller.getService(expenseId) { result ->
                editTextDateService.setText(result[0])
                val index = typeList.indexOf(result[1])
                spinnerTypeService.setSelection(index)
                editTextSumService.setText(result[2])
                editTextMileageService.setText(result[3])
            }
        }

        buttonReadyService.setOnClickListener {
            date = editTextDateService.text.toString()
            sum = if (!editTextSumService.text.isNullOrEmpty()) editTextSumService.text.toString().toDouble() else 0.0
            mileage = if (!editTextMileageService.text.isNullOrEmpty()) editTextMileageService.text.toString().toInt() else 0
            if (expenseId != null) {
                controller.updateService(expenseId, date, type, sum, mileage, carId, this)
            }
            else {
                controller.addService(date, type, sum, mileage, carId)
            }
            finish()
        }

        buttonDeleteService.setOnClickListener {
            controller.deleteService(expenseId!!, carId, this)
            finish()
        }
    }
}