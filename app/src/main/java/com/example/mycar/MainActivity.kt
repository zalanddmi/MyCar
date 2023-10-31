package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var buttonSignIn: Button
    private lateinit var buttonRegister: Button
    private lateinit var buttonSignOut: Button
    private lateinit var buttonAddCar: Button

    private lateinit var authManager: AuthManager
    private lateinit var db: FirebaseDatabase
    private lateinit var cars: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonAddCar = findViewById(R.id.buttonAddCar)

        authManager = AuthManager(this)
        db = FirebaseDatabase.getInstance()
        cars = db.getReference("Cars")


        if (authManager.isUserSignedIn()) {
            val user = authManager.getCurrentUser()
            val queryCars = cars.orderByChild("userId").equalTo(user?.uid)
            queryCars.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        GlobalScope.launch {
                            delay(500)
                            withContext(Dispatchers.Main) {
                                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        buttonSignIn.visibility = View.INVISIBLE
                        buttonRegister.visibility = View.INVISIBLE
                        buttonSignOut.visibility = View.VISIBLE
                        buttonAddCar.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибки при запросе к базе данных
                }
            })
        }
        else {
            buttonSignIn.visibility = View.VISIBLE
            buttonRegister.visibility = View.VISIBLE
            buttonSignOut.visibility = View.INVISIBLE
            buttonAddCar.visibility = View.INVISIBLE
        }
    }

    fun onButtonSignInClick(view: View) {
        val signInIntent = Intent(this, SignInActivity::class.java);
        startActivity(signInIntent);
    }

    fun onButtonRegisterClick(view: View) {
        val registerIntent = Intent(this, RegistryActivity::class.java);
        startActivity(registerIntent);
    }

    fun onButtonSignOutClick(view: View) {
        authManager.signOut()
        recreate()
    }

    fun onButtonAddCarClick(view: View) {
        val intent = Intent(this, MarkCarActivity::class.java)
        startActivity(intent)
    }
}