package com.example.babygage_ocr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.babygage_ocr.databinding.RecyclerFinancialListViewBinding


class MynumbersAdapter(val myNumbers:MutableList<Items>) : RecyclerView.Adapter<MynumbersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerFinancialListViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  // mapping myNumbers list to holder
        holder.bind(myNumbers.get(position))
    }

    override fun getItemCount(): Int {
        return myNumbers.size
    }

    inner class ViewHolder(val binding:RecyclerFinancialListViewBinding): RecyclerView.ViewHolder(binding.root){
        // make viewHolder to hold numbers
        fun bind(item:Items){
            binding.rowTextResult.text = item.item_date.toString()
            binding.rowTextResult2.text = item.item_name.toString()
            binding.rowTextResult3.text = item.item_price.toString()


        }
    }
}