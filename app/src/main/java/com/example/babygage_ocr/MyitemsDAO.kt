package com.example.babygage_ocr

import androidx.room.*

@Dao
interface MyitemsDAO {

    @Insert
    fun insertNumbers(numbers:Items) // insert values

    @Query("SELECT * FROM items")
    fun getAll(): List<Items> // Returns all values in the object List
    @Query("DELETE FROM items")
    fun deleteAllNumbers()// delete all records
}