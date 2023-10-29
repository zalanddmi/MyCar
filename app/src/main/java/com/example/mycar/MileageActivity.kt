package com.example.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MileageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mileage)

        val buttonAddMileage = findViewById<Button>(R.id.buttonAddMileage)
        val editTextMileage = findViewById<EditText>(R.id.editTextMileage)
        var mileage = 0

        buttonAddMileage.setOnClickListener {
            mileage = if (!editTextMileage.text.isNullOrEmpty()) editTextMileage.text.toString().toInt() else 0
            val mark = intent.getStringExtra("mark")
            val model = intent.getStringExtra("model")
            val intent = Intent(this, AdditionalCarActivity::class.java)
            intent.putExtra("mark", mark)
            intent.putExtra("model", model)
            intent.putExtra("mileage", mileage)
            startActivity(intent)
        }
    }
}