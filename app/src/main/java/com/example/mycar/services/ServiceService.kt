package com.example.mycar.services

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.CarDetailsActivity
import com.example.mycar.activities.HistoryActivity
import com.example.mycar.activities.HomeActivity
import com.example.mycar.entities.Expense
import com.example.mycar.entities.Service
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ServiceService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")

    fun addService(date: String, type: String, sum: Double, mileage: Int, carId: String) {
        val service = Expense(
            date = date,
            type = type,
            sum = sum,
            mileage = mileage,
            carId = carId,
            isService = true
        )
        val newService = expenses.push()
        val uKey = newService.key.toString()
        expenses.child(uKey).setValue(service)
    }

    fun deleteService(expenseId: String, carId: String, context: Context) {
        var service = expenses.child(expenseId)
        service.removeValue()
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }

    fun getService(expenseId: String, callback: (MutableList<String>) -> Unit) {
        var result: MutableList<String> = mutableListOf()
        val query = expenses.child(expenseId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    result.add(dataSnapshot.child("date").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("type").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("sum").getValue(Double::class.java).toString())
                    result.add(dataSnapshot.child("mileage").getValue(Int::class.java).toString())
                    callback(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateService(expenseId: String, date: String, type: String, sum: Double, mileage: Int, carId: String, context: Context) {
        val expense =
            Expense(
                date = date!!,
                type = type!!,
                sum = sum!!,
                mileage = mileage!!,
                carId = carId,
                isService = true,
            )
        expenses.child(expenseId).setValue(expense)
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }
}