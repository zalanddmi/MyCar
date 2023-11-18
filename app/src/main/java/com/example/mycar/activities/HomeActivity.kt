package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.services.AuthService
import com.example.mycar.CarItemAdapter
import com.example.mycar.R
import com.example.mycar.controllers.HomeController
import com.example.mycar.entities.Car
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerViewCarItems: RecyclerView
    private lateinit var buttonSignOutCarItems: Button

    private lateinit var listCarId: MutableList<String>
    private lateinit var listCar: MutableList<Car>

    private lateinit var controller: HomeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewCarItems = findViewById(R.id.recyclerViewCarItems)
        recyclerViewCarItems.layoutManager = LinearLayoutManager(this)
        buttonSignOutCarItems = findViewById(R.id.buttonSignOutCarItems)

        controller = HomeController()
        controller.getCars(this) { tripleCars ->
            listCarId = tripleCars.first
            listCar = tripleCars.second
            recyclerViewCarItems.adapter = tripleCars.third
        }

        buttonSignOutCarItems.setOnClickListener {
            controller.signOut(this)
            finish()
        }
    }
}