package com.example.mycar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mycar.R
import com.example.mycar.controllers.StartController

class StartActivity : AppCompatActivity() {
    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUp: Button
    private lateinit var buttonSignOut: Button
    private lateinit var buttonAddCar: Button

    private lateinit var controller: StartController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonAddCar = findViewById(R.id.buttonAddCar)

        controller = StartController()

        val user = controller.getUser()
        if (user != null) {
            controller.isHaveCars {isHaveCars ->
                if (isHaveCars) {
                    controller.navigateToHomeActivity(this)
                    finish()
                } else {
                    buttonSignIn.visibility = View.INVISIBLE
                    buttonSignUp.visibility = View.INVISIBLE
                    buttonSignOut.visibility = View.VISIBLE
                    buttonAddCar.visibility = View.VISIBLE
                }
            }
        }
        else {
            buttonSignIn.visibility = View.VISIBLE
            buttonSignUp.visibility = View.VISIBLE
            buttonSignOut.visibility = View.INVISIBLE
            buttonAddCar.visibility = View.INVISIBLE
        }

        buttonSignIn.setOnClickListener {
            controller.navigateToSignInActivity(this)
        }

        buttonSignUp.setOnClickListener {
            controller.navigateToSignUpActivity(this)
        }

        buttonSignOut.setOnClickListener {
            controller.signOut()
            recreate()
        }

        buttonAddCar.setOnClickListener {
            controller.navigateToMarkCarActivity(this)
        }
    }
}