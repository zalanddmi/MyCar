package com.example.mycar.controllers

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mycar.activities.HomeActivity
import com.example.mycar.activities.MarkCarActivity
import com.example.mycar.activities.SignInActivity
import com.example.mycar.activities.SignUpActivity
import com.example.mycar.services.AuthService
import com.example.mycar.services.CarsService
import com.google.firebase.auth.FirebaseUser

class StartController {
    private val _authService = AuthService()
    private val _carsService = CarsService()

    fun getUser() : FirebaseUser? = _authService.getCurrentUser()

    fun isHaveCars(callback: (Boolean) -> Unit) {
        val user = _authService.getCurrentUser()
        _carsService.isHaveCars(user) { result ->
            callback(result)
        }
    }

    fun signOut() = _authService.signOut()

    fun navigateToSignInActivity(context: Context) {
        val intent = Intent(context, SignInActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToSignUpActivity(context: Context) {
        val intent = Intent(context, SignUpActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToMarkCarActivity(context: Context) {
        val intent = Intent(context, MarkCarActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToHomeActivity(context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }
}