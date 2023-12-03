package com.example.mycar.controllers

import com.example.mycar.services.RefuelingService
import java.util.Date

class RefuelingController {
    private val _refuelingService = RefuelingService()

    fun addRefueling(date: String, typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String) {
        _refuelingService.addRefueling(date, typeFuel, sum, volume, mileage, station, carId)
    }
}