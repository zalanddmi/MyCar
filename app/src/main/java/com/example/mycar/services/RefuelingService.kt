package com.example.mycar.services

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mycar.activities.HistoryActivity
import com.example.mycar.entities.Expense
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max

class RefuelingService {
    private val db = FirebaseDatabase.getInstance()
    private val expenses = db.getReference("Expenses")
    private val cars = db.getReference("Cars")

    fun addRefueling(
        date: String, typeFuel: String, sum: Double,
        volume: Double, mileage: Int, station: String, carId: String, callback: (Boolean, String?) -> Unit) {
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        val listExpenses = mutableListOf<Pair<String, Int>>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (expenseSnapshot in dataSnapshot.children) {
                        val date = expenseSnapshot.child("date").getValue(String::class.java)!!
                        val mileageExpense = expenseSnapshot.child("mileage").getValue(Int::class.java)!!
                        listExpenses.add(Pair(date, mileageExpense))
                    }
                    val mapByDate = listExpenses.groupBy({ it.first }, { it.second })
                    val uniquePairs = mapByDate.map { (date, mileages) ->
                        date to mileages.maxOrNull()!!
                    }
                    val sortedList = uniquePairs.sortedBy { it.first }
                    if (sortedList.size == 1) {
                        val currentDate = dateFormat.parse(date).time
                        val expenseDate = dateFormat.parse(listExpenses[0].first).time
                        val expenseMileage = sortedList[0].second
                        if (currentDate >= expenseDate) {
                            if (mileage >= expenseMileage) {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val refueling = Expense(
                                            date = date,
                                            type = typeFuel,
                                            sum = sum,
                                            volume = volume,
                                            mileage = mileage,
                                            station = station,
                                            carId = carId,
                                            isService = false
                                        )
                                        val newRefueling = expenses.push()
                                        val uKey = newRefueling.key.toString()
                                        expenses.child(uKey).setValue(refueling)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                            else {
                                callback(false, "Пробег должен быть не меньше $expenseMileage км")
                            }
                        }
                        else {
                            if (mileage <= expenseMileage) {
                                val refueling = Expense(
                                    date = date,
                                    type = typeFuel,
                                    sum = sum,
                                    volume = volume,
                                    mileage = mileage,
                                    station = station,
                                    carId = carId,
                                    isService = false
                                )
                                val newRefueling = expenses.push()
                                val uKey = newRefueling.key.toString()
                                expenses.child(uKey).setValue(refueling)
                                callback(true, null)
                            }
                            else {
                                callback(false, "Пробег должен быть не больше $expenseMileage км")
                            }
                        }
                    }
                    else {
                        var minMileage = Int.MAX_VALUE
                        var maxMileage = Int.MIN_VALUE
                        val currentDate = dateFormat.parse(date).time
                        var listMileages = mutableListOf<Int>()
                        for (expense in listExpenses) {
                            val expenseDate = dateFormat.parse(expense.first).time
                            if (currentDate >= expenseDate) {
                                if (minMileage == Int.MAX_VALUE) {
                                    minMileage = expense.second
                                }
                                else {
                                    minMileage = max(minMileage, expense.second)
                                }
                            }
                            else {
                                maxMileage = expense.second
                            }
                            listMileages.add(expense.second)
                        }
                        Log.d("minMileage", minMileage.toString())
                        Log.d("maxMileage", maxMileage.toString())
                        Log.d("1", "${listExpenses[0].first} - ${listExpenses[0].second}")
                        Log.d("2", "${listExpenses[1].first} - ${listExpenses[1].second}")
                        if (minMileage == Int.MAX_VALUE) {
                            if (mileage > maxMileage) {
                                callback(false, "Пробег должен быть не более $maxMileage км")
                            }
                            else {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val refueling = Expense(
                                            date = date,
                                            type = typeFuel,
                                            sum = sum,
                                            volume = volume,
                                            mileage = mileage,
                                            station = station,
                                            carId = carId,
                                            isService = false
                                        )
                                        val newRefueling = expenses.push()
                                        val uKey = newRefueling.key.toString()
                                        expenses.child(uKey).setValue(refueling)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                        }
                        else if (maxMileage == Int.MIN_VALUE){
                            if (mileage < minMileage) {
                                callback(false, "Пробег должен быть не меньше $minMileage км")
                            }
                            else {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val refueling = Expense(
                                            date = date,
                                            type = typeFuel,
                                            sum = sum,
                                            volume = volume,
                                            mileage = mileage,
                                            station = station,
                                            carId = carId,
                                            isService = false
                                        )
                                        val newRefueling = expenses.push()
                                        val uKey = newRefueling.key.toString()
                                        expenses.child(uKey).setValue(refueling)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                        }
                        else {
                            if (mileage in minMileage .. maxMileage) {
                                val refueling = Expense(
                                    date = date,
                                    type = typeFuel,
                                    sum = sum,
                                    volume = volume,
                                    mileage = mileage,
                                    station = station,
                                    carId = carId,
                                    isService = false
                                )
                                val newRefueling = expenses.push()
                                val uKey = newRefueling.key.toString()
                                expenses.child(uKey).setValue(refueling)
                                callback(true, null)
                            }
                            else {
                                callback(false, "Пробег должен быть в пределах $minMileage - $maxMileage км")
                            }
                        }
                    }
                }
                else {
                    val queryCars = cars.child(carId)
                    queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val mileageCar = snapshot.child("mileage").getValue(Int::class.java)!!
                            if (mileageCar >= mileage) {
                                callback(false, "Пробег должен быть от $mileageCar км")
                            } else {
                                val refueling = Expense(
                                    date = date,
                                    type = typeFuel,
                                    sum = sum,
                                    volume = volume,
                                    mileage = mileage,
                                    station = station,
                                    carId = carId,
                                    isService = false
                                )
                                val newRefueling = expenses.push()
                                val uKey = newRefueling.key.toString()
                                expenses.child(uKey).setValue(refueling)
                                snapshot.ref.child("mileage").setValue(mileage)
                                callback(true, null)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            callback(false, "Ошибка при добавлении")
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false, "Ошибка при добавлении")
            }
        })
        /*
        val refueling = Expense(
            date = date,
            type = typeFuel,
            sum = sum,
            volume = volume,
            mileage = mileage,
            station = station,
            carId = carId,
            isService = false
        )
        val newRefueling = expenses.push()
        val uKey = newRefueling.key.toString()
        expenses.child(uKey).setValue(refueling)

         */
    }

    fun deleteRefueling(expenseId: String, carId: String, context: Context) {
        var refueling = expenses.child(expenseId)
        refueling.removeValue()
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
    }

    fun getRefueling(expenseId: String, callback: (MutableList<String>) -> Unit) {
        var result: MutableList<String> = mutableListOf()
        val query = expenses.child(expenseId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    result.add(dataSnapshot.child("date").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("type").getValue(String::class.java)!!)
                    result.add(dataSnapshot.child("sum").getValue(Double::class.java).toString())
                    result.add(dataSnapshot.child("volume").getValue(Double::class.java).toString())
                    result.add(dataSnapshot.child("mileage").getValue(Int::class.java).toString())
                    result.add(dataSnapshot.child("station").getValue(String::class.java)!!)
                    callback(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateRefueling(expenseId: String, date: String, type: String, sum: Double, volume: Double, mileage: Int,
                      station: String, carId: String, context: Context, callback: (Boolean, String?) -> Unit) {
        val queryExpenses = expenses.orderByChild("carId").equalTo(carId)
        val listExpenses = mutableListOf<Pair<String, Int>>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        queryExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (expenseSnapshot in dataSnapshot.children) {
                        val date = expenseSnapshot.child("date").getValue(String::class.java)!!
                        val mileageExpense = expenseSnapshot.child("mileage").getValue(Int::class.java)!!
                        listExpenses.add(Pair(date, mileageExpense))
                    }
                    val mapByDate = listExpenses.groupBy({ it.first }, { it.second })
                    val uniquePairs = mapByDate.map { (date, mileages) ->
                        date to mileages.maxOrNull()!!
                    }
                    val sortedList = uniquePairs.sortedBy { it.first }
                    if (sortedList.size == 1) {
                        val currentDate = dateFormat.parse(date).time
                        val expenseDate = dateFormat.parse(listExpenses[0].first).time
                        val expenseMileage = sortedList[0].second
                        if (currentDate >= expenseDate) {
                            if (mileage >= expenseMileage) {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val expense =
                                            Expense(
                                                date!!,
                                                type!!,
                                                sum!!,
                                                volume!!,
                                                mileage!!,
                                                station!!,
                                                carId,
                                                isService = false,
                                                expenseId
                                            )
                                        expenses.child(expenseId).setValue(expense)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                        val intent = Intent(context, HistoryActivity::class.java)
                                        intent.putExtra("carId", carId)
                                        context.startActivity(intent)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                            else {
                                callback(false, "Пробег должен быть не меньше $expenseMileage км")
                            }
                        }
                        else {
                            if (mileage <= expenseMileage) {
                                val expense =
                                    Expense(
                                        date!!,
                                        type!!,
                                        sum!!,
                                        volume!!,
                                        mileage!!,
                                        station!!,
                                        carId,
                                        isService = false,
                                        expenseId
                                    )
                                expenses.child(expenseId).setValue(expense)
                                callback(true, null)
                                val intent = Intent(context, HistoryActivity::class.java)
                                intent.putExtra("carId", carId)
                                context.startActivity(intent)
                            }
                            else {
                                callback(false, "Пробег должен быть не больше $expenseMileage км")
                            }
                        }
                    }
                    else {
                        var minMileage = Int.MAX_VALUE
                        var maxMileage = Int.MIN_VALUE
                        val currentDate = dateFormat.parse(date).time
                        var listMileages = mutableListOf<Int>()
                        for (expense in listExpenses) {
                            val expenseDate = dateFormat.parse(expense.first).time
                            if (currentDate >= expenseDate) {
                                if (minMileage == Int.MAX_VALUE) {
                                    minMileage = expense.second
                                }
                                else {
                                    minMileage = max(minMileage, expense.second)
                                }
                            }
                            else {
                                maxMileage = expense.second
                            }
                            listMileages.add(expense.second)
                        }
                        Log.d("minMileage", minMileage.toString())
                        Log.d("maxMileage", maxMileage.toString())
                        Log.d("1", "${listExpenses[0].first} - ${listExpenses[0].second}")
                        Log.d("2", "${listExpenses[1].first} - ${listExpenses[1].second}")
                        if (minMileage == Int.MAX_VALUE) {
                            if (mileage > maxMileage) {
                                callback(false, "Пробег должен быть не более $maxMileage км")
                            }
                            else {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val expense =
                                            Expense(
                                                date!!,
                                                type!!,
                                                sum!!,
                                                volume!!,
                                                mileage!!,
                                                station!!,
                                                carId,
                                                isService = false,
                                                expenseId
                                            )
                                        expenses.child(expenseId).setValue(expense)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                        val intent = Intent(context, HistoryActivity::class.java)
                                        intent.putExtra("carId", carId)
                                        context.startActivity(intent)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                        }
                        else if (maxMileage == Int.MIN_VALUE){
                            if (mileage < minMileage) {
                                callback(false, "Пробег должен быть не меньше $minMileage км")
                            }
                            else {
                                val queryCars = cars.child(carId)
                                queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val expense =
                                            Expense(
                                                date!!,
                                                type!!,
                                                sum!!,
                                                volume!!,
                                                mileage!!,
                                                station!!,
                                                carId,
                                                isService = false,
                                                expenseId
                                            )
                                        expenses.child(expenseId).setValue(expense)
                                        snapshot.ref.child("mileage").setValue(mileage)
                                        callback(true, null)
                                        val intent = Intent(context, HistoryActivity::class.java)
                                        intent.putExtra("carId", carId)
                                        context.startActivity(intent)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false, "Ошибка при добавлении")
                                    }
                                })
                            }
                        }
                        else {
                            if (mileage in minMileage .. maxMileage) {
                                val expense =
                                    Expense(
                                        date!!,
                                        type!!,
                                        sum!!,
                                        volume!!,
                                        mileage!!,
                                        station!!,
                                        carId,
                                        isService = false,
                                        expenseId
                                    )
                                expenses.child(expenseId).setValue(expense)
                                callback(true, null)
                                val intent = Intent(context, HistoryActivity::class.java)
                                intent.putExtra("carId", carId)
                                context.startActivity(intent)
                            }
                            else {
                                callback(false, "Пробег должен быть в пределах $minMileage - $maxMileage км")
                            }
                        }
                    }
                }
                else {
                    val queryCars = cars.child(carId)
                    queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val mileageCar = snapshot.child("mileage").getValue(Int::class.java)!!
                            if (mileageCar >= mileage) {
                                callback(false, "Пробег должен быть от $mileageCar км")
                            } else {
                                val expense =
                                    Expense(
                                        date!!,
                                        type!!,
                                        sum!!,
                                        volume!!,
                                        mileage!!,
                                        station!!,
                                        carId,
                                        isService = false,
                                        expenseId
                                    )
                                expenses.child(expenseId).setValue(expense)
                                snapshot.ref.child("mileage").setValue(mileage)
                                callback(true, null)
                                val intent = Intent(context, HistoryActivity::class.java)
                                intent.putExtra("carId", carId)
                                context.startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            callback(false, "Ошибка при добавлении")
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false, "Ошибка при добавлении")
            }
        })
    }

    /*
            val expense =
            Expense(
                date!!,
                type!!,
                sum!!,
                volume!!,
                mileage!!,
                station!!,
                carId,
                isService = false,
                expenseId
            )
        expenses.child(expenseId).setValue(expense)
        val intent = Intent(context, HistoryActivity::class.java)
        intent.putExtra("carId", carId)
        context.startActivity(intent)
     */
}