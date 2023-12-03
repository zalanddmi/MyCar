package com.example.mycar.services

import com.example.mycar.entities.Service
import com.google.firebase.database.FirebaseDatabase

class ServiceService {
    private val db = FirebaseDatabase.getInstance()
    private val services = db.getReference("Services")

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String) {
        val service = Service(date, type, sum, mileage, carId)
        val newService = services.push()
        val uKey = newService.key.toString()
        services.child(uKey).setValue(service)
    }
}