package com.example.babygage_ocr

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities=[Items::class], version=1)
abstract class MyitemsDB : RoomDatabase() {
    abstract fun mynumbersDAO(): MyitemsDAO


    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: MyitemsDB?=null

        fun getInstance(context: Context):MyitemsDB{
            return INSTANCE ?: synchronized(this){
                // use Room's database builder to create a RoomDatabase object
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyitemsDB::class.java,
                    "mynumbers_database" // create a RoomDatabase object "mynumbers_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }

}