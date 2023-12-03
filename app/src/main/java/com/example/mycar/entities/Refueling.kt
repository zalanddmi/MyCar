package com.example.mycar.entities

import java.util.Date

data class Refueling(val date: String, val typeFuel: String, val sum: Double,
    val volume: Double, val mileage: Int, val station: String, val carId: String)
