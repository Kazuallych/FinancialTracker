package com.example.financialtracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialtracker.databinding.ActivityMainBinding
import com.example.financialtracker.ui.theme.FinancialTrackerTheme

class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Item>
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList()
        data.add(Item(1000,false))

        adapter = Adapter(data)
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                val newItem = Item(result.data?.getStringExtra("numberAdd")!!.toInt(),false)
                data.add(newItem)
                adapter.notifyItemInserted(data.size-1)
            }
        }


        binding.btAdd.setOnClickListener {
            launcher?.launch(Intent(this, EditShablon::class.java))
        }
    }
}