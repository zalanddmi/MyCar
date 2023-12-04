package com.example.mycar.services

import com.example.mycar.entities.Expense
import com.example.mycar.entities.Service
import com.google.firebase.database.FirebaseDatabase

class ServiceService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String) {
        val service = Expense(
            date = date,
            type = type,
            sum = sum,
            mileage = mileage,
            carId = carId,
            isService = true
        )
        val newService = expenses.push()
        val uKey = newService.key.toString()
        expenses.child(uKey).setValue(service)
    }
}