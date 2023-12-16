package com.example.mycar.services

import android.content.Context
import android.content.Intent
import com.example.mycar.adapters.CarItemAdapter
import com.example.mycar.activities.CarDetailsActivity
import com.example.mycar.activities.HomeActivity
import com.example.mycar.entities.Car
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CarsService {
    private val db = FirebaseDatabase.getInstance()
    private val cars = db.getReference("Cars")
    private val expenses = db.getReference("Expenses")

    fun isHaveCars(user: FirebaseUser?, callback: (Boolean) -> Unit) {
        val queryCars = cars.orderByChild("userId").equalTo(user?.uid)
        queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }


    fun getCarDetails(carId: String, callback: (MutableList<String>) -> Unit) {
        var result: MutableList<String> = mutableListOf()
        val query = cars.child(carId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    result.add(dataSnapshot.child("mark").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("model").getValue(String::class.java)!!)
                    result.add("${dataSnapshot.child("mileage").getValue(Int::class.java)} км")
                    result.add(if (dataSnapshot.child("carYear").getValue(Int::class.java) != 0) dataSnapshot.child("carYear").getValue(Int::class.java).toString() else "")
                    result.add(if (dataSnapshot.child("vin").exists()) dataSnapshot.child("vin").getValue(String::class.java)!! else "")
                    result.add(if (dataSnapshot.child("typeBody").exists()) dataSnapshot.child("typeBody").getValue(String::class.java)!! else "")
                    result.add(if (dataSnapshot.child("color").exists()) dataSnapshot.child("color").getValue(String::class.java)!! else "")
                    result.add(if (dataSnapshot.child("stateNumber").exists()) dataSnapshot.child("stateNumber").getValue(String::class.java)!! else "")
                    result.add(if (dataSnapshot.child("typeFuel").exists()) dataSnapshot.child("typeFuel").getValue(String::class.java)!! else "")
                    result.add(if (dataSnapshot.child("volumeFuel").getValue(Int::class.java) != 0) dataSnapshot.child("volumeFuel").getValue(Int::class.java).toString() else "")

                    callback(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



    fun getCarsHome(user: FirebaseUser?, context: Context, callback: (Triple<MutableList<String>, MutableList<Car>, CarItemAdapter>) -> Unit) {
        var listCarId: MutableList<String> = mutableListOf()
        var listCar: MutableList<Car> = mutableListOf()
        lateinit var adapter: CarItemAdapter
        val queryCars = cars.orderByChild("userId").equalTo(user?.uid)
        queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (carSnapshot in dataSnapshot.children) {
                    val carId = carSnapshot.key
                    val mark = carSnapshot.child("mark").getValue(String::class.java)
                    val model = carSnapshot.child("model").getValue(String::class.java)
                    val mileage = carSnapshot.child("mileage").getValue(Int::class.java)
                    val car = Car(mark!!, model!!, mileage!!)
                    listCarId.add(carId!!)
                    listCar.add(car)
                }
                adapter = CarItemAdapter(listCar, object : CarItemAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val carId = listCarId[position]
                        val intent = Intent(context, CarDetailsActivity::class.java)
                        intent.putExtra("carId", carId)
                        context.startActivity(intent)
                    }
                })

                callback.invoke(Triple(listCarId, listCar, adapter))
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun deleteCar(carId: String, context: Context) {
        var car = cars.child(carId)
        car.removeValue()
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }
}