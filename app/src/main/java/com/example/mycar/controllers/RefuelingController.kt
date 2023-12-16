package com.example.mycar.controllers

import android.content.Context
import com.example.mycar.services.RefuelingService
import java.util.Date

class RefuelingController {
    private val _refuelingService = RefuelingService()

    fun addRefueling(date: String, typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String) {
        _refuelingService.addRefueling(date, typeFuel, sum, volume, mileage, station, carId)
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
                        station: String, carId: String, context: Context) {
        _refuelingService.updateRefueling(expenseId, date, type, sum, volume, mileage, station, carId, context)
    }
}