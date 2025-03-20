package com.pizza.kkomdae.ui.step1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pizza.kkomdae.data.Step1Result
import com.pizza.kkomdae.databinding.ItemStep1ResultBinding

class Step1ResultAdapter(val list: List<Step1Result>): RecyclerView.Adapter<Step1ResultAdapter.Step1ResultViewHolder>() {
    inner class Step1ResultViewHolder(val binding: ItemStep1ResultBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Step1Result){
            Glide.with(binding.ivPosition)
                .load(item.image)
                .into(binding.ivPosition)

            binding.tvPosition.text= item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Step1ResultViewHolder {
        return Step1ResultViewHolder(
            ItemStep1ResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: Step1ResultViewHolder, position: Int) {

        holder.bind(list[position])
    }
}