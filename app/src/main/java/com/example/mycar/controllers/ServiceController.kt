package com.example.mycar.controllers

import android.content.Context
import com.example.mycar.services.ServiceService

class ServiceController {
    private val _serviceService = ServiceService()

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String) {
        _serviceService.addService(date, type, sum, mileage, carId)
    }

    fun deleteService(expenseId: String, carId: String, context: Context) {
        _serviceService.deleteService(expenseId, carId, context)
    }

    fun getService(expenseId: String, callback: (MutableList<String>) -> Unit) {
        _serviceService.getService(expenseId) { result ->
            callback.invoke(result)
        }
    }

    fun updateService(expenseId: String, date: String, type: String, sum: Double, mileage: Int, carId: String, context: Context) {
        _serviceService.updateService(expenseId, date, type, sum, mileage, carId, context)
    }
}