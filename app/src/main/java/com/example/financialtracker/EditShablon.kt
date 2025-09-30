package com.example.financialtracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import com.example.financialtracker.databinding.EditShablonActivityBinding

class EditShablon : ComponentActivity() {
    lateinit var binding: EditShablonActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditShablonActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("code")=="1"){
            binding.edText.setText(intent.getStringExtra("edNumber").toString())
        }

        val items = listOf("Доход","Расход")
        var selectedItem = String()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btDone.setOnClickListener {
            if(binding.edText.text.toString()==""){
                binding.edText.error = "Не все поля заполены!"
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra("numberAdd",binding.edText.text.toString())
            intent.putExtra("transactionAdd",selectedItem)
            setResult(RESULT_OK,intent)
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }
}