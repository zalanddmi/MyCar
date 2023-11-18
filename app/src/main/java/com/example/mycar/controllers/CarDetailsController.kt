package com.example.mycar.controllers

import com.example.mycar.services.AuthService
import com.example.mycar.services.CarsService

class CarDetailsController {
    private val _authService = AuthService()
    private val _carsService = CarsService()

    fun getCarDetails(carId: String, callback: (MutableList<String>) -> Unit) {
        _carsService.getCarDetails(carId) { result ->
            callback.invoke(result)
        }
    }
}