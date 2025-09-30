package com.example.financialtracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialtracker.databinding.ActivityMainBinding


class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Item>
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var editLauncher:ActivityResultLauncher<Intent>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var position = 0

        data = ArrayList()

        adapter = Adapter(data,
            {
                position->
                data.removeAt(position)
                adapter.notifyItemRemoved(position)
                sum()
            },{
                item ->
                position = data.indexOf(item)
                val i = Intent(this, EditShablon::class.java)
                i.putExtra("edNumber",data[position].number.toString())
                i.putExtra("code","1")
                editLauncher?.launch(i)
            })
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                val newItem = Item(result.data?.getStringExtra("numberAdd")!!.toInt(),false)
                if(result.data?.getStringExtra("transactionAdd")=="Доход"){
                    newItem.transaction =true
                }else{
                    newItem.number*=-1
                }
                data.add(newItem)
                adapter.notifyItemInserted(data.size-1)
                sum()
            }
        }

        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            if(result.resultCode ==RESULT_OK){
                data[position].number = result.data?.getStringExtra("numberAdd")!!.toInt()
                if(result.data?.getStringExtra("transactionAdd")=="Доход"){

                    if(data[position].number<0)
                        data[position].number*=-1

                    data[position].transaction = true
                }else{
                    data[position].number*=-1
                    data[position].transaction = false
                }
                adapter.notifyItemChanged(position,adapter.itemCount)
                sum()
            }
        }
        binding.btAdd.setOnClickListener {
            launcher?.launch(Intent(this, EditShablon::class.java))
        }
        sum()
    }
    fun sum(){
        if(data.isNotEmpty()){
            var sum = 0
            for(i in data){
                sum += i.number
                Log.d("MyLog",sum.toString())
            }
            binding.tvSum.text = "Общая сумма: $sum"
        }else{
            binding.tvSum.text = "Общая сумма: 0"
        }
    }

}