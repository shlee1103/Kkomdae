package com.pizza.kkomdae.ui.step3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.pizza.kkomdae.R
import com.pizza.kkomdae.data.Step1Result
import com.pizza.kkomdae.databinding.ItemStep1ResultBinding

class AiResultAdapter(val list: List<Step1Result>, val listen: (Int) -> Unit) :
    RecyclerView.Adapter<AiResultAdapter.AiResultViewHolder>() {

    private var selectedPosition: Int = 0

    inner class AiResultViewHolder(val binding: ItemStep1ResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            Glide.with(binding.ivPosition)
                .load(list[position].image)
                .into(binding.ivPosition)

            if (position == selectedPosition) {
                binding.root.scaleX = 1.2f
                binding.root.scaleY = 1.2f
                (binding.root as MaterialCardView).setStrokeColor(
                    ContextCompat.getColor(binding.root.context, R.color.blue500)
                )
            } else {
                binding.root.scaleX = 1.0f
                binding.root.scaleY = 1.0f
                (binding.root as MaterialCardView).setStrokeColor(
                    ContextCompat.getColor(binding.root.context, R.color.gray200)
                )
            }

            binding.tvPosition.text = list[position].name

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                listen(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiResultViewHolder {
        val binding = ItemStep1ResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AiResultViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AiResultViewHolder, position: Int) {
        holder.bind(position)
    }
}