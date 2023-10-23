package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.mycar.models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistryActivity : AppCompatActivity(), AuthManager.AuthCallback {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registry)

        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        authManager = AuthManager(this)
        authManager.setAuthCallback(this)
    }

    fun onButtonSignUpClick(view: View) {
        signUpUser(view)
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
        authManager.signUp(editTextFirstName.text.toString(), editTextLastName.text.toString(), editTextEmail.text.toString(), editTextPassword.text.toString(), view)
    }

    override fun onSuccess() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}