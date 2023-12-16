package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.example.mycar.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import okhttp3.*
import java.io.IOException

class ModelCarActivity : AppCompatActivity() {
    private lateinit var client: OkHttpClient
    private lateinit var gson: Gson
    private lateinit var modelList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_car)

        var carId = intent.getStringExtra("carId")
        var mileage = intent.getIntExtra("mileage", 0)

        client = OkHttpClient()
        gson = Gson()
        modelList = mutableListOf()

        val listViewModel = findViewById<ListView>(R.id.listViewModel)
        val modelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, modelList)
        listViewModel.adapter = modelAdapter

        val selectedMarkId = intent.getStringExtra("selectedMarkId")
        val selectedMarkName = intent.getStringExtra("selectedMarkName")

        val modelRequest = Request.Builder()
            .url("https://cars-base.ru/api/cars/$selectedMarkId")
            .build()

        client.newCall(modelRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Обработка ошибки
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val modelResponseData = response.body?.string()
                    val modelJsonArray = gson.fromJson(modelResponseData, JsonArray::class.java)

                    val modelList = modelJsonArray.map {
                        val name = it.asJsonObject.get("name").asString
                        name
                    }.toMutableList()

                    runOnUiThread {
                        modelAdapter.clear()
                        modelAdapter.addAll(modelList)
                        modelAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                modelAdapter.filter.filter(newText)
                return true
            }
        })

        listViewModel.setOnItemClickListener { _, _, position, _ ->
            val model = modelAdapter.getItem(position)
            lateinit var intent: Intent
            if (carId != null) {
                intent = Intent(this, AdditionalCarActivity::class.java)
                intent.putExtra("carId", carId)
                intent.putExtra("mileage", mileage)
            }
            else {
                intent = Intent(this, MileageActivity::class.java)
            }
            intent.putExtra("mark", selectedMarkName)
            intent.putExtra("model", model)
            startActivity(intent)
        }
    }
}