package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.mycar.services.AuthService
import com.example.mycar.R
import com.example.mycar.controllers.SignInController
import com.google.android.material.snackbar.Snackbar

class SignInActivity : AppCompatActivity(){
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignInUser: Button

    private lateinit var controller: SignInController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignInUser = findViewById(R.id.buttonSignInUser)

        controller = SignInController(this)

        buttonSignInUser.setOnClickListener {view ->
            signInUser(view)
        }
    }

    private fun signInUser(view: View) {
        if (editTextEmail.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (editTextPassword.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите пароль", Snackbar.LENGTH_SHORT).show()
            return
        }
        controller.signIn(editTextEmail.text.toString(), editTextPassword.text.toString(), view)
    }
}