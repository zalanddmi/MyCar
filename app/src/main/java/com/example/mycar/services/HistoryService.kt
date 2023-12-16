package com.example.mycar.services

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mycar.activities.CarDetailsActivity
import com.example.mycar.activities.RefuelingActivity
import com.example.mycar.activities.ServiceActivity
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
                            isService!!,
                            expenseId
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
                            isService = isService!!,
                            expenseId = expenseId
                        )
                    }
                    Log.d("CZH", expense.isService.toString())
                    listHistoryId.add(Pair(isService!!, expenseId!!))
                    listHistory.add(expense)
                }
                listHistory = listHistory.sortedByDescending {it.mileage}.toMutableList()
                val sortedListHistory = listHistory.sortedByDescending { it.mileage }
                val sortedListHistoryId = listHistoryId.toMutableList()
                adapter = HistoryItemAdapter(listHistory, object : HistoryItemAdapter.OnItemClickListener {

                    override fun onItemClick(position: Int) {
                        val expenseId = listHistory[position].expenseId
                        val isService = listHistory[position].isService
                        lateinit var intent: Intent
                        if (isService) {
                            intent = Intent(context, ServiceActivity::class.java)
                        }
                        else {
                            intent = Intent(context, RefuelingActivity::class.java)
                        }
                        intent.putExtra("carId", carId)
                        intent.putExtra("expenseId", expenseId)
                        context.startActivity(intent)
                    }
                })

                callback.invoke(Triple(sortedListHistoryId.toMutableList(), sortedListHistory.toMutableList(), adapter))
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}