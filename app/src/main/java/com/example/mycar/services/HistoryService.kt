package com.example.mycar.services

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mycar.adapters.HistoryItemAdapter
import com.example.mycar.entities.Car
import com.example.mycar.entities.Expense
import com.example.mycar.entities.Refueling
import com.example.mycar.entities.Service
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")

    fun getHistory(carId: String, context: Context,
                    callback: (Triple<MutableList<Pair<Boolean, String>>, MutableList<Expense>, HistoryItemAdapter>) -> Unit) {
        var listHistoryId = mutableListOf<Pair<Boolean, String>>()
        var listHistory = mutableListOf<Expense>()
        lateinit var adapter: HistoryItemAdapter
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("CZH", dataSnapshot.exists().toString())
                for (expenseSnapshot in dataSnapshot.children) {
                    val expenseId = expenseSnapshot.key
                    val date = expenseSnapshot.child("date").getValue(String::class.java)
                    val type = expenseSnapshot.child("type").getValue(String::class.java)
                    val sum = expenseSnapshot.child("sum").getValue(Double::class.java)
                    val volume = expenseSnapshot.child("volume").getValue(Double::class.java)
                    val mileage = expenseSnapshot.child("mileage").getValue(Int::class.java)
                    val station = expenseSnapshot.child("station").getValue(String::class.java)
                    val isService = expenseSnapshot.child("service").getValue(Boolean::class.java)
                    lateinit var expense: Expense
                    if (!isService!!) {
                        expense =
                        Expense(
                            date!!,
                            type!!,
                            sum!!,
                            volume!!,
                            mileage!!,
                            station!!,
                            carId,
                            isService!!
                        )
                    }
                    else {
                        expense =
                        Expense(
                            date = date!!,
                            type = type!!,
                            sum = sum!!,
                            mileage = mileage!!,
                            carId = carId,
                            isService = isService!!
                        )
                    }
                    Log.d("CZH", expense.isService.toString())
                    listHistoryId.add(Pair(isService!!, expenseId!!))
                    listHistory.add(expense)
                }
                listHistory = listHistory.sortedByDescending {it.mileage}.toMutableList()
                adapter = HistoryItemAdapter(listHistory, object : HistoryItemAdapter.OnItemClickListener {

                    override fun onItemClick(position: Int) {
                        val carId = listHistoryId[position]
                    }
                })

                callback.invoke(Triple(listHistoryId, listHistory, adapter))
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}