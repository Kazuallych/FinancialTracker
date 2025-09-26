package com.example.financialtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.financialtracker.databinding.EditShablonActivityBinding

class EditShablon : ComponentActivity() {
    lateinit var binding: EditShablonActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditShablonActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}