package com.example.financialtracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialtracker.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.mutableListOf


class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Item>
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var editLauncher:ActivityResultLauncher<Intent>?=null
    lateinit var db: MainDB

    var entries = mutableListOf<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList()
        var position = 0
        db = MainDB.getDb(this)
        sum()
        lifecycleScope.launch {
            data.addAll(ArrayList(db.getDao().getAllData()))
            //Настройка адаптера,удаления и запуска изменения эелемента
            adapter = Adapter(data,
                {
                        position->
                    lifecycleScope.launch {
                        db.getDao().delete(data[position])
                        data.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        sum()
                    }
                },{
                        item ->
                    position = data.indexOf(item)
                    val i = Intent(this@MainActivity, EditShablon::class.java)
                    i.putExtra("edNumber",data[position].number.toString())
                    i.putExtra("code","1")
                    editLauncher?.launch(i)
                })

            binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rcView.adapter = adapter
            sum()
        }

        //Создание нового элемента
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                val newItem = Item(null,result.data?.getStringExtra("numberAdd")!!.toInt(),false)
                if(result.data?.getStringExtra("transactionAdd")=="Доход"){
                    newItem.transaction =true
                }else{
                    newItem.number*=-1
                }
                lifecycleScope.launch {
                    db.getDao().insert(newItem)
                    data.clear()
                    data.addAll(db.getDao().getAllData())

                    withContext(Dispatchers.Main){
                        adapter.notifyItemInserted(data.size-1)
                        sum()
                    }
                }
            }
        }
        //Изменение элемента
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            if(result.resultCode ==RESULT_OK){
                lifecycleScope.launch {
                    data[position].number = result.data?.getStringExtra("numberAdd")!!.toInt()
                    if(result.data?.getStringExtra("transactionAdd")=="Доход"){
                        if(data[position].number<0)
                            data[position].number*=-1

                        data[position].transaction = true
                    }else{
                        data[position].number*=-1
                        data[position].transaction = false
                    }
                    db.getDao().update(data[position])
                    adapter.notifyItemChanged(position,adapter.itemCount)
                    sum()
                }

            }
        }
        //Добавление элемента
        binding.btAdd.setOnClickListener {
            launcher?.launch(Intent(this, EditShablon::class.java))
        }
    }
    //Подсчет суммы всех элементов
    fun sum(){
        var sum = 0
        if(data.isNotEmpty()){
            for(i in data){
                sum += i.number
            }
            binding.tvSum.text = "Общая сумма: $sum"
        }else{
            binding.tvSum.text = "Общая сумма: 0"
        }

        if(data.isEmpty()){
            entries =mutableListOf()
            entries.add(Entry(0.toFloat(),0.toFloat()))
        }else{
            entries.add(Entry(entries.size.toFloat(),sum.toFloat()))
        }

        val dataSet = LineDataSet(entries, "График")
        dataSet.setColor("#00e600".toColorInt())
        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.xAxis.apply {
            axisMinimum = 0f
            granularity = 1f
        }
        binding.lineChart.invalidate()
    }

}