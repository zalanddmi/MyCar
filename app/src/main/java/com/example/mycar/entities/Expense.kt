package com.example.mycar.entities

data class Expense(
    val date: String,
    val type: String,
    val sum: Double,
    val volume: Double? = null,
    val mileage: Int,
    val station: String? = null,
    val carId: String,
    val isService: Boolean,
    val expenseId: String? = null
)
