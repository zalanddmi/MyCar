package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.mycar.R
import com.example.mycar.controllers.AdditionalCarController
import com.example.mycar.entities.Car
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class AdditionalCarActivity : AppCompatActivity() {
    private lateinit var editTextCarYear: EditText
    private lateinit var editTextVin: EditText
    private lateinit var spinnerTypeBody: Spinner
    private lateinit var spinnerColor: Spinner
    private lateinit var editTextStateNumber: EditText
    private lateinit var spinnerTypeFuel: Spinner
    private lateinit var editTextVolumeFuel: EditText
    private lateinit var buttonReadyCar: Button

    private lateinit var controller: AdditionalCarController

    private var carYear: Int = 0
    private var vin: String? = null
    private var typeBody: String? = null
    private var color: String? = null
    private var stateNumber: String? = null
    private var typeFuel: String? = null
    private var volumeFuel: Int = 0

    private lateinit var typeBodyList: MutableList<String>
    private lateinit var colorList: MutableList<String>
    private lateinit var typeFuelList: MutableList<String>

    private lateinit var db: FirebaseDatabase
    private lateinit var cars: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_car)

        var carId = intent.getStringExtra("carId")

        editTextCarYear = findViewById(R.id.editTextCarYear)
        editTextVin = findViewById(R.id.editTextVin)
        spinnerTypeBody = findViewById(R.id.spinnerTypeBody)
        spinnerColor = findViewById(R.id.spinnerColor)
        editTextStateNumber = findViewById(R.id.editTextStateNumber)
        spinnerTypeFuel = findViewById(R.id.spinnerTypeFuel)
        editTextVolumeFuel = findViewById(R.id.editTextVolumeFuel)
        buttonReadyCar = findViewById(R.id.buttonReadyCar)

        controller = AdditionalCarController()

        db = FirebaseDatabase.getInstance()
        cars = db.getReference("Cars")

        typeBodyList = mutableListOf("Выберете тип кузова", "Седан", "Универсал", "Хэтчбек", "Лифтбэк",
            "Купе", "Лимузин", "Кабриолет", "Внедорожник")
        colorList = mutableListOf("Выберете цвет", "Красный", "Зеленый", "Синий", "Желтый", "Оранжевый",
            "Фиолетовый", "Розовый", "Коричневый", "Серый", "Черный", "Белый", "Циан",
            "Пурпурный", "Лайм", "Бирюзовый", "Индиго")
        typeFuelList = mutableListOf("Выберете вид топлива", "Бензин", "Дизель", "Газ", "Газ + Бензин",
            "Электричество", "Гибрид")

        val typeBodyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeBodyList)
        typeBodyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeBody.adapter = typeBodyAdapter

        val colorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorList)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColor.adapter = colorAdapter
        val typeFuelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeFuelList)
        typeFuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeFuel.adapter = typeFuelAdapter

        spinnerTypeBody.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                typeBody = if (position != 0) typeBodyList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        spinnerColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                color = if (position != 0) colorList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        spinnerTypeFuel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                typeFuel = if (position != 0) typeFuelList[position] else ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        if (carId != null) {
            controller.getCarDetails(carId!!) { details ->
                editTextCarYear.setText(details[3])
                editTextVin.setText(details[4])
                var index = typeBodyList.indexOf(details[5])
                spinnerTypeBody.setSelection(index)
                index = colorList.indexOf(details[6])
                spinnerColor.setSelection(index)
                editTextStateNumber.setText(details[7])
                index = typeFuelList.indexOf(details[8])
                spinnerTypeFuel.setSelection(index)
                editTextVolumeFuel.setText(details[9])
            }
        }

        buttonReadyCar.setOnClickListener {
            val carYearText = editTextCarYear.text.toString()
            carYear = if (carYearText.isNotEmpty()) {
                val year = carYearText.toIntOrNull()
                if (year != null && year in 1900..Calendar.getInstance().get(Calendar.YEAR)) {
                    year
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Введите корректный год",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else {
                0
            }

            val vinText = editTextVin.text.toString()
            vin = if (vinText.isNotEmpty()) {
                if (vinText.length == 17) {
                    vinText
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Введите корректный VIN (17 символов)",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else {
                null
            }

            val volumeFuelText = editTextVolumeFuel.text.toString()
            volumeFuel = if (volumeFuelText.isNotEmpty()) {
                try {
                    volumeFuelText.toInt()
                } catch (e: NumberFormatException) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Введите корректный объем топлива (целое число)",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else {
                0
            }
            stateNumber = editTextStateNumber.text.toString().ifEmpty { null }
            val mark = intent.getStringExtra("mark").toString()
            val model = intent.getStringExtra("model").toString()
            val mileage = intent.getIntExtra("mileage", 0)

            val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val car = Car(mark, model, mileage, userId, carYear, vin, typeBody, color, stateNumber, typeFuel, volumeFuel)
            lateinit var intent: Intent
            if (carId != null) {
                cars.child(carId).setValue(car)
                intent = Intent(this, CarDetailsActivity::class.java)
                intent.putExtra("carId", carId)
                startActivity(intent)
                finish()
            }
            else {
                val newCar = cars.push()
                val uKey = newCar.key.toString()
                cars.child(uKey).setValue(car)
                intent = Intent(this, CarDetailsActivity::class.java)
                intent.putExtra("carId", uKey)
                startActivity(intent)
                finish()
            }
        }
    }
}