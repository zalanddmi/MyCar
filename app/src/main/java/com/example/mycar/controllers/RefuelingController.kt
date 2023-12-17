package com.example.mycar.controllers

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.CarDetailsActivity
import com.example.mycar.activities.MarkCarActivity
import com.example.mycar.services.RefuelingService

class RefuelingController {
    private val _refuelingService = RefuelingService()

    fun addRefueling(date: String, typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String, callback: (Boolean, String?) -> Unit) {
        _refuelingService.addRefueling(date, typeFuel, sum, volume, mileage, station, carId) { flag, text ->
            callback.invoke(flag, text)
        }
    }

    fun deleteRefueling(expenseId: String, carId: String, context: Context) {
        _refuelingService.deleteRefueling(expenseId, carId, context)
    }

    fun getService(expenseId: String, callback: (MutableList<String>) -> Unit) {
        _refuelingService.getRefueling(expenseId) { result ->
            callback.invoke(result)
        }
    }

    fun updateRefueling(expenseId: String, date: String, type: String, sum: Double, volume: Double, mileage: Int,
                        station: String, carId: String, context: Context, callback: (Boolean, String?) -> Unit) {
        _refuelingService.updateRefueling(expenseId, date, type, sum, volume, mileage, station, carId, context) { flag, text ->
            callback.invoke(flag, text)
        }
    }

    fun navigateToDetails(carId: String, context: Context) {
        val intent = Intent(context, CarDetailsActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }
}