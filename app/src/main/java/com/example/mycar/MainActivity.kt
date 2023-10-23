package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var buttonSignIn: Button
    private lateinit var buttonRegister: Button
    private lateinit var buttonSignOut: Button

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonSignOut = findViewById(R.id.buttonSignOut)

        authManager = AuthManager(this)

        if (authManager.isUserSignedIn()) {
            buttonSignIn.visibility = View.INVISIBLE
            buttonRegister.visibility = View.INVISIBLE
            buttonSignOut.visibility = View.VISIBLE
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
}