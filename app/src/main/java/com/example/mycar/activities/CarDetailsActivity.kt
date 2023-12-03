package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mycar.R
import com.example.mycar.controllers.CarDetailsController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CarDetailsActivity : AppCompatActivity() {
    private lateinit var textViewMarkCarDetailsValue: TextView
    private lateinit var textViewModelCarDetailsValue: TextView
    private lateinit var textViewMileageCarDetailsValue: TextView
    private lateinit var textViewCarYearCarDetailsValue: TextView
    private lateinit var textViewVinCarDetailsValue: TextView
    private lateinit var textViewTypeBodyCarDetailsValue: TextView
    private lateinit var textViewColorCarDetailsValue: TextView
    private lateinit var textViewStateNumberCarDetailsValue: TextView
    private lateinit var textViewTypeFuelCarDetailsValue: TextView
    private lateinit var textViewVolumeFuelCarDetailsValue: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var controller: CarDetailsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        textViewMarkCarDetailsValue = findViewById(R.id.textViewMarkCarDetailsValue)
        textViewModelCarDetailsValue = findViewById(R.id.textViewModelCarDetailsValue)
        textViewMileageCarDetailsValue = findViewById(R.id.textViewMileageCarDetailsValue)
        textViewCarYearCarDetailsValue = findViewById(R.id.textViewCarYearCarDetailsValue)
        textViewVinCarDetailsValue = findViewById(R.id.textViewVinCarDetailsValue)
        textViewTypeBodyCarDetailsValue = findViewById(R.id.textViewTypeBodyCarDetailsValue)
        textViewColorCarDetailsValue = findViewById(R.id.textViewColorCarDetailsValue)
        textViewStateNumberCarDetailsValue = findViewById(R.id.textViewStateNumberCarDetailsValue)
        textViewTypeFuelCarDetailsValue = findViewById(R.id.textViewTypeFuelCarDetailsValue)
        textViewVolumeFuelCarDetailsValue = findViewById(R.id.textViewVolumeFuelCarDetailsValue)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        controller = CarDetailsController()
        val carId = intent.getStringExtra("carId")

        controller.getCarDetails(carId!!) { details ->
            textViewMarkCarDetailsValue.text = details[0]
            textViewModelCarDetailsValue.text = details[1]
            textViewMileageCarDetailsValue.text = details[2]
            textViewCarYearCarDetailsValue.text = details[3]
            textViewVinCarDetailsValue.text = details[4]
            textViewTypeBodyCarDetailsValue.text = details[5]
            textViewColorCarDetailsValue.text = details[6]
            textViewStateNumberCarDetailsValue.text = details[7]
            textViewTypeFuelCarDetailsValue.text = details[8]
            textViewVolumeFuelCarDetailsValue.text = details[9]
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_refueling -> {
                    val intent = Intent(this, RefuelingActivity::class.java)
                    intent.putExtra("carId", carId)
                    startActivity(intent)
                    true
                }
                R.id.action_service -> {
                    val intent = Intent(this, ServiceActivity::class.java)
                    intent.putExtra("carId", carId)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}