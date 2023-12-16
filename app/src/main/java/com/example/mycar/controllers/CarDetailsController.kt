package com.example.mycar.controllers

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.HomeActivity
import com.example.mycar.activities.MarkCarActivity
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

    fun updateCar(carId: String, mileage: Int, context: Context) {
        val intent = Intent(context, MarkCarActivity::class.java)
        intent.putExtra("carId", carId)
        intent.putExtra("mileage", mileage)
        context.startActivity(intent)
    }

    fun deleteCar(carId: String, context: Context) {
        _carsService.deleteCar(carId, context)
    }

    fun backToHome(context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }
}