package com.example.financialtracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Insert
    suspend fun insert(contact: Item)

    @Query("SELECT*FROM item_table")
    suspend fun getAllData(): List<Item>

    @Delete
    suspend fun delete(contact: Item)

    @Update
    suspend fun update(contact: Item)
}