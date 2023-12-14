package com.example.mycar.controllers

import com.example.mycar.services.StatisticsService

class StatisticsController {
    private val _statisticsService = StatisticsService()

    fun getExpenses(carId: String, from: String, to: String, callback: (Triple<String, String, String>) -> Unit) {
        _statisticsService.getExpenses(carId, from, to) { result ->
            callback.invoke(result)
        }
    }

    fun getMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        _statisticsService.getMileage(carId, from, to) { result ->
            callback.invoke(result)
        }
    }

    fun getFuelMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        _statisticsService.getFuelMileage(carId, from, to) { result ->
            callback.invoke(result)
        }
    }

    fun getRefuelingMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        _statisticsService.getRefuelingMileage(carId, from, to) { result ->
            callback.invoke(result)
        }
    }

    fun getRefuelingDay(carId: String, from: String, to: String, callback: (String) -> Unit) {
        _statisticsService.getRefuelingDay(carId, from, to) { result ->
            callback.invoke(result)
        }
    }
}