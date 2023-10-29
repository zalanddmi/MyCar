package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
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

        client = OkHttpClient()
        gson = Gson()
        markMap = mutableMapOf()
        markList = mutableListOf()
        /*
        val spinnerMark = findViewById<Spinner>(R.id.spinnerMark)
        val spinnerModel = findViewById<Spinner>(R.id.spinnerModel)

        val markAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, markList)
        markAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMark.adapter = markAdapter

        val modelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ArrayList<String>())
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerModel.adapter = modelAdapter

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

        spinnerMark.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedMarkName = markList[position]
                val selectedMarkId = markMap[selectedMarkName]

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
                            val modelJsonArray =
                                gson.fromJson(modelResponseData, JsonArray::class.java)

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
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Обработка случая, когда ничего не выбрано
            }
        }
        */
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
            intent.putExtra("selectedMarkId", selectedMarkId)
            intent.putExtra("selectedMarkName", selectedMarkName)
            startActivity(intent)
        }
    }
}