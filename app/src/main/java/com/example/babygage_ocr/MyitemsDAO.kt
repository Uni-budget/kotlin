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

    @Query("SELECT * FROM items WHERE items.user_id == :userId")
    fun findId(userId: String): List<Items>

    @Query("DELETE FROM items WHERE items.user_id == :userId")
    fun deleteIdNumbers(userId: String)// delete all records

}