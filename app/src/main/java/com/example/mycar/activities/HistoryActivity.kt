package com.example.mycar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.R
import com.example.mycar.controllers.HistoryController
import com.example.mycar.entities.Expense

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerViewHistoryItems: RecyclerView

    private lateinit var listHistoryId: MutableList<Pair<Boolean, String>>
    private lateinit var listHistory: MutableList<Expense>
    private lateinit var carId: String

    private lateinit var controller: HistoryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerViewHistoryItems = findViewById(R.id.recyclerViewHistoryItems)
        recyclerViewHistoryItems.layoutManager = LinearLayoutManager(this)

        listHistoryId = mutableListOf()
        listHistory = mutableListOf()
        carId = intent.getStringExtra("carId")!!

        controller = HistoryController()
        controller.getHistory(carId!!, this) { tripleHistory ->
            listHistoryId = tripleHistory.first
            listHistory = tripleHistory.second
            val adapter = tripleHistory.third
            recyclerViewHistoryItems.adapter = adapter
        }
    }

    override fun onBackPressed() {
        controller.backToCarDetails(carId, this)
        finish()
    }
}