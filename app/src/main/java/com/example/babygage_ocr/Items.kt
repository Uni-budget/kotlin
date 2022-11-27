package com.example.babygage_ocr

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Items(@PrimaryKey var idx:Int, var item_date:String, var item_name:String, var item_price:String)
