package com.example.mycar

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

class RegistryActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registry)

        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
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
        auth.createUserWithEmailAndPassword(editTextEmail.text.toString(), editTextPassword.text.toString())
            .addOnSuccessListener { authResult ->
                val user = User(editTextFirstName.text.toString(), editTextLastName.text.toString(), editTextEmail.text.toString(), editTextPassword.text.toString())
                users.child(authResult.user!!.uid)
                    .setValue(user)
                    .addOnSuccessListener {
                        Snackbar.make(view, "Вы зарегистрированы!", Snackbar.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
    }
}