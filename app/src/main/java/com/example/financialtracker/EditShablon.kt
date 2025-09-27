package com.example.financialtracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import com.example.financialtracker.databinding.EditShablonActivityBinding

class EditShablon : ComponentActivity() {
    lateinit var binding: EditShablonActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditShablonActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Доход","Расход")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.btDone.setOnClickListener {
            var intent = Intent()
            intent.putExtra("numberAdd",binding.edText.text.toString())
            setResult(RESULT_OK,intent)
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }
}