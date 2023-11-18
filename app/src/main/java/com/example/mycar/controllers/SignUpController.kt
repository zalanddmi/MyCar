package com.example.mycar.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.mycar.activities.StartActivity
import com.example.mycar.services.AuthService

class SignUpController(private val context: Context) : AuthService.AuthCallback {
    private val _authService = AuthService().apply {
        setAuthCallback(this@SignUpController)
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String, view: View) {
        _authService.signUp(firstName, lastName, email, password, view)
    }

    override fun onSuccess() {
        if (context is Activity) {
            context.finish()
        }
        val intent = Intent(context, StartActivity::class.java)
        context.startActivity(intent)
    }
}