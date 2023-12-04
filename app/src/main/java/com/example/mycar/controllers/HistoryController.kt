package com.example.mycar.controllers

import android.content.Context
import com.example.mycar.adapters.HistoryItemAdapter
import com.example.mycar.entities.Expense
import com.example.mycar.services.HistoryService

class HistoryController {
    private val _historyService = HistoryService()

    fun getHistory(carId: String, context: Context,
                   callback: (Triple<MutableList<Pair<Boolean, String>>, MutableList<Expense>, HistoryItemAdapter>) -> Unit) {
        _historyService.getHistory(carId, context) { result ->
            callback.invoke(result)
        }
    }
}