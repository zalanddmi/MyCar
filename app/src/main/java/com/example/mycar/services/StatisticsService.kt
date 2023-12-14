package com.example.mycar.services

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatisticsService {
    private val db = FirebaseDatabase.getInstance()
    private val cars = db.getReference("Cars")
    private val expenses = db.getReference("Expenses")

    fun getExpenses(carId: String, from: String, to: String, callback: (Triple<String, String, String>) -> Unit) {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fromDate = format.parse(from)
        val toDate = format.parse(to)
        var refuelingSum = 0.0
        var serviceSum = 0.0
        var totalSum = 0.0
        var refuelingSumString = "0 рублей (100%)"
        var serviceSumString = "0 рублей (100%)"
        var totalSumString = "0 рублей"
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (expenseSnapshot in dataSnapshot.children) {
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val sum = expenseSnapshot.child("sum").getValue(Double::class.java)
                    val isService = expenseSnapshot.child("service").getValue(Boolean::class.java)
                    val dateDate = format.parse(date)
                    if (dateDate != null && (dateDate in fromDate..toDate)) {
                        if (isService!!) {
                            serviceSum += sum!!
                        }
                        else {
                            refuelingSum += sum!!
                        }
                        totalSum += sum!!
                    }
                }
                if (totalSum != 0.0) {
                    if (refuelingSum != 0.0) {
                        refuelingSumString = "$refuelingSum рублей (${"%.2f".format(refuelingSum / totalSum * 100)} %)"
                    }
                    if (serviceSum != 0.0) {
                        serviceSumString = "$serviceSum рублей (${"%.2f".format(serviceSum / totalSum * 100)} %)"
                    }
                    totalSumString = "$totalSum рублей"
                }
                callback.invoke(Triple(refuelingSumString, serviceSumString, totalSumString))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при получении данных: ${databaseError.message}")
            }
        })
    }

    fun getMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fromDate = format.parse(from)
        val toDate = format.parse(to)
        var mileage = 0
        var mileageString = ""
        var listMileage = mutableListOf<Int>()
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (expenseSnapshot in dataSnapshot.children) {
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val mileage = expenseSnapshot.child("mileage").getValue(Int::class.java)
                    val dateDate = format.parse(date)
                    if (dateDate != null && (dateDate in fromDate..toDate)) {
                        listMileage.add(mileage!!)
                    }
                }
                listMileage.sortDescending()
                if (listMileage.size > 1) {
                    mileage = listMileage[0] - listMileage[listMileage.size - 1]
                }
                mileageString = "$mileage км"
                callback.invoke(mileageString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при получении данных: ${databaseError.message}")
            }
        })
    }

    fun getFuelMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fromDate = format.parse(from)
        val toDate = format.parse(to)
        val listFuelMileage = mutableListOf<Pair<Int, Double>>()
        var fuelMileageString = ""
        var fuelMileage = 0.0
        var fuel = 0.0
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (expenseSnapshot in dataSnapshot.children) {
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val mileage = expenseSnapshot.child("mileage").getValue(Int::class.java)
                    val volume = expenseSnapshot.child("volume").getValue(Double::class.java)
                    val isService = expenseSnapshot.child("service").getValue(Boolean::class.java)
                    val dateDate = format.parse(date)
                    if (dateDate != null && (dateDate in fromDate..toDate)) {
                        if (!isService!!) {
                            listFuelMileage.add(Pair(mileage!!, volume!!))
                        }
                    }
                }
                listFuelMileage.sortByDescending { it.first }
                if (listFuelMileage.size > 1) {
                    listFuelMileage.forEach {
                        fuel += it.second
                    }
                    fuel -= listFuelMileage[0].second
                    fuelMileage = fuel / (listFuelMileage[0].first - listFuelMileage[listFuelMileage.size - 1].first) * 100
                }
                fuelMileageString = "${"%.2f".format(fuelMileage)} л/100 км"
                callback.invoke(fuelMileageString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при получении данных: ${databaseError.message}")
            }
        })
    }

    fun getRefuelingMileage(carId: String, from: String, to: String, callback: (String) -> Unit) {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fromDate = format.parse(from)
        val toDate = format.parse(to)
        var listMileage = mutableListOf<Int>()
        var refuelingMileageString = ""
        var refuelingMileage = 0
        var mileage = 0
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (expenseSnapshot in dataSnapshot.children) {
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val mileage = expenseSnapshot.child("mileage").getValue(Int::class.java)
                    val isService = expenseSnapshot.child("service").getValue(Boolean::class.java)
                    val dateDate = format.parse(date)
                    if (dateDate != null && (dateDate in fromDate..toDate)) {
                        if (!isService!!) {
                            listMileage.add(mileage!!)
                        }
                    }
                }
                listMileage.sortDescending()
                if (listMileage.size > 1) {
                    mileage = listMileage[0] - listMileage[listMileage.size - 1]
                    refuelingMileage = mileage / (listMileage.size - 1)
                }
                refuelingMileageString = "$refuelingMileage км"
                callback.invoke(refuelingMileageString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при получении данных: ${databaseError.message}")
            }
        })
    }

    fun getRefuelingDay(carId: String, from: String, to: String, callback: (String) -> Unit) {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fromDate = format.parse(from)
        val toDate = format.parse(to)
        var listRefuelingDay = mutableListOf<Date>()
        var refuelingDayString = ""
        var refuelingDay = 0.0
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (expenseSnapshot in dataSnapshot.children) {
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val isService = expenseSnapshot.child("service").getValue(Boolean::class.java)
                    val dateDate = format.parse(date)
                    if (dateDate != null && (dateDate in fromDate..toDate)) {
                        if (!isService!!) {
                            listRefuelingDay.add(dateDate)
                        }
                    }
                }
                listRefuelingDay.sort()
                if (listRefuelingDay.size > 1) {
                    for (i in 1 until listRefuelingDay.size) {
                        refuelingDay += (listRefuelingDay[i].time - listRefuelingDay[i - 1].time) / (1000 * 60 * 60 * 24)
                    }
                }
                refuelingDayString = "${"%.2f".format(refuelingDay / (listRefuelingDay.size - 1))} д"
                callback.invoke(refuelingDayString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при получении данных: ${databaseError.message}")
            }
        })
    }
}