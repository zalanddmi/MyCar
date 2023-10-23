package com.example.mycar

import android.content.Context
import android.view.View
import com.example.mycar.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthManager(private val context: Context) {
    interface AuthCallback {
        fun onSuccess()
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val users = db.getReference("Users")

    private var authCallback: AuthCallback? = null

    fun setAuthCallback(callback: AuthCallback) {
        authCallback = callback
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String, view: View) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = User(firstName, lastName, email, password)
                users.child(authResult.user!!.uid)
                    .setValue(user)
                    .addOnSuccessListener {
                        Snackbar.make(view, "Вы зарегистрированы!", Snackbar.LENGTH_SHORT).show()
                        authCallback?.onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
    }

    fun signIn(email: String, password: String, view: View) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Snackbar.make(view, "Вы вошли!", Snackbar.LENGTH_SHORT).show()
                authCallback?.onSuccess()
            }
            .addOnFailureListener { e ->
                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }
}