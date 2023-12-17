package com.example.mycar.controllers

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.CarDetailsActivity
import com.example.mycar.services.ServiceService

class ServiceController {
    private val _serviceService = ServiceService()

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String, callback: (Boolean, String?) -> Unit) {
        _serviceService.addService(date, type, sum, mileage, carId) { flag, text ->
            callback.invoke(flag, text)
        }
    }

    fun deleteService(expenseId: String, carId: String, context: Context) {
        _serviceService.deleteService(expenseId, carId, context)
    }

    fun getService(expenseId: String, callback: (MutableList<String>) -> Unit) {
        _serviceService.getService(expenseId) { result ->
            callback.invoke(result)
        }
    }

    fun updateService(expenseId: String, date: String, type: String, sum: Double, mileage: Int, carId: String, context: Context, callback: (Boolean, String?) -> Unit) {
        _serviceService.updateService(expenseId, date, type, sum, mileage, carId, context) { flag, text ->
            callback.invoke(flag, text)
        }
    }

    fun navigateToDetails(carId: String, context: Context) {
        val intent = Intent(context, CarDetailsActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }
}