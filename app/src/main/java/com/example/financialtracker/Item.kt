package com.example.financialtracker

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo(name = "Number")
    var number: Int,
    @ColumnInfo(name = "Transaction")
    var transaction: Boolean)
