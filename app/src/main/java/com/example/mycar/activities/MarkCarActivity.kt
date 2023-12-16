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

class MarkCarActivity : AppCompatActivity() {
    private lateinit var client: OkHttpClient
    private lateinit var gson: Gson
    private lateinit var markMap: MutableMap<String, String>
    private lateinit var markList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_car)

        var carId = intent.getStringExtra("carId")
        var mileage = intent.getIntExtra("mileage", 0)

        client = OkHttpClient()
        gson = Gson()
        markMap = mutableMapOf()
        markList = mutableListOf()

        val markListView = findViewById<ListView>(R.id.listViewMark)
        val markAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, markList)
        markListView.adapter = markAdapter

        val markRequest = Request.Builder()
            .url("https://cars-base.ru/api/cars/")
            .build()

        client.newCall(markRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Обработка ошибки
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonArray = gson.fromJson(responseData, JsonArray::class.java)

                    for (jsonObject in jsonArray) {
                        val id = jsonObject.asJsonObject.get("id").asString
                        val name = jsonObject.asJsonObject.get("name").asString
                        markMap[name] = id
                        markList.add(name)
                    }

                    runOnUiThread {
                        markAdapter.notifyDataSetChanged()
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
                markAdapter.filter.filter(newText)
                return true
            }
        })

        markListView.setOnItemClickListener { _, _, position, _ ->
            val selectedMarkName = markAdapter.getItem(position)
            val selectedMarkId = markMap[selectedMarkName]

            // Передайте выбранную марку в следующую Activity и откройте ее.
            val intent = Intent(this, ModelCarActivity::class.java)
            if (carId != null) {
                intent.putExtra("carId", carId)
                intent.putExtra("mileage", mileage)
            }
            intent.putExtra("selectedMarkId", selectedMarkId)
            intent.putExtra("selectedMarkName", selectedMarkName)
            startActivity(intent)
        }
    }
}