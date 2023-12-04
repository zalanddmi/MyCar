package com.example.mycar.services

import com.example.mycar.entities.Expense
import com.example.mycar.entities.Refueling
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

class RefuelingService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")

    fun addRefueling(date: String,  typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String) {
        val refueling = Expense(
            date = date,
            type = typeFuel,
            sum = sum,
            volume = volume,
            mileage = mileage,
            station = station,
            carId = carId,
            isService = false
        )
        val newRefueling = expenses.push()
        val uKey = newRefueling.key.toString()
        expenses.child(uKey).setValue(refueling)
    }
}