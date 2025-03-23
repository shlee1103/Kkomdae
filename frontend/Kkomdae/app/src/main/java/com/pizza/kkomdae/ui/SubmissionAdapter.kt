package com.pizza.kkomdae.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pizza.kkomdae.R
import com.pizza.kkomdae.data.Submission
import com.pizza.kkomdae.databinding.ItemSubmissionBinding


class SubmissionAdapter: ListAdapter<Submission, SubmissionAdapter.SubmissionViewHolder>(object : DiffUtil.ItemCallback<Submission>(){
    override fun areContentsTheSame(oldItem: Submission, newItem: Submission): Boolean {
        return oldItem==newItem
    }

    override fun areItemsTheSame(oldItem: Submission, newItem: Submission): Boolean {
        return oldItem === newItem
    }
}){


    inner class SubmissionViewHolder(val binding: ItemSubmissionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.btnDown.setOnClickListener {
                binding.tvState.isVisible = false
                binding.clMenu.isVisible=true
                binding.btnUp.isVisible=true
                binding.btnDown.isVisible=false
            }

            binding.btnUp.setOnClickListener {
                binding.tvState.isVisible = true
                binding.clMenu.isVisible=false
                binding.btnUp.isVisible=false
                binding.btnDown.isVisible=true
            }
        }
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        return SubmissionViewHolder(ItemSubmissionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}
