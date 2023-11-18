package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.mycar.services.AuthService
import com.example.mycar.R
import com.example.mycar.controllers.SignUpController
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity(){

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUpUser: Button

    private lateinit var controller: SignUpController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUpUser = findViewById(R.id.buttonSignUpUser)

        controller = SignUpController(this)

        buttonSignUpUser.setOnClickListener { view ->
            signUpUser(view)
        }
    }

    private fun signUpUser(view: View) {
        if (editTextFirstName.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите ваше имя", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (editTextLastName.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите вашу фамилию", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (editTextEmail.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (editTextPassword.text.toString().isEmpty()) {
            Snackbar.make(view, "Введите пароль", Snackbar.LENGTH_SHORT).show()
            return
        }
        controller.signUp(editTextFirstName.text.toString(), editTextLastName.text.toString(), editTextEmail.text.toString(), editTextPassword.text.toString(), view)
    }
}