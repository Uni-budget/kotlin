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

    @Query("SELECT * FROM items WHERE items.user_id == :userId and items.category == :category")
    fun findId(userId: String, category:String): List<Items>
    @Query("SELECT * FROM items WHERE items.user_id == :userId")
    fun findIdOnly(userId: String): List<Items>

    @Query("SELECT * FROM items WHERE items.user_id == :userId and items.year_month == :yearMonth and items.category == :category")
    fun findIdDate(userId: String, yearMonth:String, category: String): List<Items>

    @Query("DELETE FROM items WHERE items.user_id == :userId and items.category == :category")
    fun deleteIdNumbers(userId: String, category:String)// delete all records

}