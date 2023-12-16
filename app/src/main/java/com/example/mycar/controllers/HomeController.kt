package com.example.mycar.controllers

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.MarkCarActivity
import com.example.mycar.adapters.CarItemAdapter
import com.example.mycar.activities.StartActivity
import com.example.mycar.entities.Car
import com.example.mycar.services.AuthService
import com.example.mycar.services.CarsService

class HomeController {
    private val _authService = AuthService()
    private val _carsService = CarsService()

    fun getCars(context: Context, callback: (Triple<MutableList<String>, MutableList<Car>, CarItemAdapter>) -> Unit) {
        val user = _authService.getCurrentUser()
        _carsService.getCarsHome(user, context) { result ->
            callback.invoke(result)
        }
    }

    fun signOut(context: Context) {
        _authService.signOut()
        val intent = Intent(context, StartActivity::class.java)
        context.startActivity(intent)
    }

    fun addCar(context: Context) {
        val intent = Intent(context, MarkCarActivity::class.java)
        context.startActivity(intent)
    }
}