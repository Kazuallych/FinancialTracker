package com.example.financialtracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class MainDB: RoomDatabase() {
    abstract fun getDao(): Dao
    companion object{
        fun getDb(context: Context): MainDB{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "contact3.db"
            ).build()
        }
    }

}