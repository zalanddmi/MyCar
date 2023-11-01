package com.example.mycar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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

    private lateinit var db: FirebaseDatabase
    private lateinit var cars: DatabaseReference

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

        db = FirebaseDatabase.getInstance()
        cars = db.getReference("Cars")
        val carId = intent.getStringExtra("carId")
        val query = cars.child(carId!!)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    textViewMarkCarDetailsValue.text = dataSnapshot.child("mark").getValue(String::class.java)
                    textViewModelCarDetailsValue.text = dataSnapshot.child("model").getValue(String::class.java)
                    textViewMileageCarDetailsValue.text = "${dataSnapshot.child("mileage").getValue(Int::class.java)} км"
                    textViewCarYearCarDetailsValue.text = dataSnapshot.child("carYear").getValue(Int::class.java).toString()
                    textViewVinCarDetailsValue.text = if (dataSnapshot.child("vin").exists()) dataSnapshot.child("vin").getValue(String::class.java) else ""
                    textViewTypeBodyCarDetailsValue.text = if (dataSnapshot.child("typeBody").exists()) dataSnapshot.child("typeBody").getValue(String::class.java) else ""
                    textViewColorCarDetailsValue.text = if (dataSnapshot.child("color").exists()) dataSnapshot.child("color").getValue(String::class.java) else ""
                    textViewStateNumberCarDetailsValue.text = if (dataSnapshot.child("stateNumber").exists()) dataSnapshot.child("stateNumber").getValue(String::class.java) else ""
                    textViewTypeFuelCarDetailsValue.text = if (dataSnapshot.child("typeFuel").exists()) dataSnapshot.child("typeFuel").getValue(String::class.java) else ""
                    textViewVolumeFuelCarDetailsValue.text = "${dataSnapshot.child("volumeFuel").getValue(Int::class.java)} л"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}