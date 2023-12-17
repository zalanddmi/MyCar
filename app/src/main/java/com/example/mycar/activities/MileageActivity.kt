package com.example.mycar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.mycar.R
import com.google.android.material.snackbar.Snackbar

class MileageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mileage)

        val buttonAddMileage = findViewById<Button>(R.id.buttonAddMileage)
        val editTextMileage = findViewById<EditText>(R.id.editTextMileage)

        buttonAddMileage.setOnClickListener {
            val mileageText = editTextMileage.text.toString()

            val mileage = if (mileageText.isNotEmpty() && mileageText.toInt() >= 0) {
                try {
                    mileageText.toInt()
                } catch (e: NumberFormatException) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Введите корректное значение для пробега",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else {
                if (mileageText.isNotEmpty() && mileageText.toInt() < 0) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Введите корректное значение для пробега",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                else {
                    0
                }
            }

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