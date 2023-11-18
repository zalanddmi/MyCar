package com.example.mycar.entities

data class Car(val mark: String, val model: String, val mileage: Int, val userId: String? = null, val carYear: Int? = 0, val vin: String? = null,
               val typeBody: String? = null, val color: String? = null, val stateNumber: String? = null,
               val typeFuel: String? = null, val volumeFuel: Int? = 0)
