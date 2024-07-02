package com.example.MyApplication1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication1.databinding.EachItemViewBinding


class RvAdapter(val context:Context, var data:ArrayList<Modal>):RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    fun setFilteredData(filteredData:ArrayList<Modal>){
        data=filteredData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= EachItemViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.name.text= data[position].name
            holder.binding.symbol.text= data[position].symbol
            holder.binding.price.text= data[position].price

    }
    inner class ViewHolder(val binding:EachItemViewBinding):RecyclerView.ViewHolder(binding.root)

}