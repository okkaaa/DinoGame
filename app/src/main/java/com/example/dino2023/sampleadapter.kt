package com.example.dino2023

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dino2023.databinding.RowBinding

class SampleAdapter (val items : MutableList<SampleModel>)
    : RecyclerView.Adapter<SampleAdapter.ViewHolder>(){
    private lateinit var binding: RowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding=RowBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SampleAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount() = items.size
    inner class ViewHolder(itemView : RowBinding) : RecyclerView.ViewHolder(itemView.root){
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item : SampleModel){
            binding.apply {
                tvId.text=item.id.toString()
                tvName.text=item.name

            }
        }

    }
}