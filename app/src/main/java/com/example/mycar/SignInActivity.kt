package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.mycar.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity(), AuthManager.AuthCallback {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        authManager = AuthManager(this)
        authManager.setAuthCallback(this)
    }

    fun onButtonSignInClick(view: View) {
        signInUser(view)
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
        authManager.signIn(editTextEmail.text.toString(), editTextPassword.text.toString(), view)
    }

    override fun onSuccess() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}