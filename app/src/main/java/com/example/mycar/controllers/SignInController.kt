package com.example.mycar.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.mycar.activities.StartActivity
import com.example.mycar.services.AuthService

class SignInController(private val context: Context) : AuthService.AuthCallback {
    private val _authService = AuthService().apply {
        setAuthCallback(this@SignInController)
    }

    fun signIn(email: String, password: String, view: View) {
        _authService.signIn(email, password, view)
    }

    override fun onSuccess() {
        if (context is Activity) {
            context.finish()
        }
        val intent = Intent(context, StartActivity::class.java)
        context.startActivity(intent)
    }
}