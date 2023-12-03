package com.example.mycar.controllers

import com.example.mycar.services.ServiceService

class ServiceController {
    private val _serviceService = ServiceService()

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String) {
        _serviceService.addService(date, type, sum, mileage, carId)
    }
}