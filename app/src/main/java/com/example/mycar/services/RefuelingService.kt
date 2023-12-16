package com.example.mycar.services

import android.content.Context
import android.content.Intent
import com.example.mycar.activities.HistoryActivity
import com.example.mycar.entities.Expense
import com.example.mycar.entities.Refueling
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class RefuelingService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")

    fun addRefueling(date: String,  typeFuel: String, sum: Double,
                     volume: Double, mileage: Int, station: String, carId: String) {
        val refueling = Expense(
            date = date,
            type = typeFuel,
            sum = sum,
            volume = volume,
            mileage = mileage,
            station = station,
            carId = carId,
            isService = false
        )
        val newRefueling = expenses.push()
        val uKey = newRefueling.key.toString()
        expenses.child(uKey).setValue(refueling)
    }

    fun deleteRefueling(expenseId: String, carId: String, context: Context) {
        var refueling = expenses.child(expenseId)
        refueling.removeValue()
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }

    fun getRefueling(expenseId: String, callback: (MutableList<String>) -> Unit) {
        var result: MutableList<String> = mutableListOf()
        val query = expenses.child(expenseId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    result.add(dataSnapshot.child("date").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("type").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("sum").getValue(Double::class.java).toString())
                    result.add(dataSnapshot.child("volume").getValue(Double::class.java).toString())
                    result.add(dataSnapshot.child("mileage").getValue(Int::class.java).toString())
                    result.add(dataSnapshot.child("station").getValue(String::class.java)!!)
                    callback(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateRefueling(expenseId: String, date: String, type: String, sum: Double, volume: Double, mileage: Int,
                      station: String, carId: String, context: Context) {
        val expense =
            Expense(
                date!!,
                type!!,
                sum!!,
                volume!!,
                mileage!!,
                station!!,
                carId,
                isService = false,
                expenseId
            )
        expenses.child(expenseId).setValue(expense)
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }
}