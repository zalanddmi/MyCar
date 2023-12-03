package com.example.mycar.services

import com.example.mycar.entities.Refueling
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

class RefuelingService {
    private val db = FirebaseDatabase.getInstance()
    private val refuelings = db.getReference("Refuelings")

    fun addRefueling(date: String,  typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String) {
        val refueling = Refueling(date, typeFuel, sum, volume, mileage, station, carId)
        val newRefueling = refuelings.push()
        val uKey = newRefueling.key.toString()
        refuelings.child(uKey).setValue(refueling)
    }
}