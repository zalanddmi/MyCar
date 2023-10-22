package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonSignInClick(view: View) {
        val signInIntent = Intent(this, SignInActivity::class.java);
        startActivity(signInIntent);
    }

    fun onButtonRegisterClick(view: View) {
        val registerIntent = Intent(this, RegistryActivity::class.java);
        startActivity(registerIntent);
    }
}