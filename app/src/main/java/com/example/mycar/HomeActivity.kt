package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.models.Car
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerViewCarItems: RecyclerView
    private lateinit var buttonSignOutCarItems: Button

    private lateinit var authManager: AuthManager
    private lateinit var db: FirebaseDatabase
    private lateinit var cars: DatabaseReference

    private lateinit var listCarId: MutableList<String>
    private lateinit var listCar: MutableList<Car>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewCarItems = findViewById(R.id.recyclerViewCarItems)
        recyclerViewCarItems.layoutManager = LinearLayoutManager(this)
        buttonSignOutCarItems = findViewById(R.id.buttonSignOutCarItems)

        authManager = AuthManager(this)
        db = FirebaseDatabase.getInstance()
        cars = db.getReference("Cars")

        listCarId = mutableListOf()
        listCar = mutableListOf()
        val queryCars = cars.orderByChild("userId").equalTo(authManager.getCurrentUser()?.uid)
        queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCarId = mutableListOf()
                listCar = mutableListOf()

                for (carSnapshot in dataSnapshot.children) {
                    val carId = carSnapshot.key
                    val mark = carSnapshot.child("mark").getValue(String::class.java)
                    val model = carSnapshot.child("model").getValue(String::class.java)
                    val mileage = carSnapshot.child("mileage").getValue(Int::class.java)
                    val car = Car(mark!!, model!!, mileage!!)
                    listCarId.add(carId!!)
                    listCar.add(car)
                }

                val adapter = CarItemAdapter(listCar, object : CarItemAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val carId = listCarId[position]
                        val intent = Intent(this@HomeActivity, CarDetailsActivity::class.java)
                        intent.putExtra("carId", carId)
                        startActivity(intent)
                    }
                })
                recyclerViewCarItems.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при запросе к базе данных
            }
        })

        buttonSignOutCarItems.setOnClickListener {
            authManager.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}