package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSet
import com.example.yugioh_tcg_deckmaster.databinding.ItemTestBinding

class TestAdapter(private val viewModel: MainViewModel) :
    RecyclerView.Adapter<TestAdapter.ItemViewHolder>() {

    private var dataset: List<YugiohSet> = listOf()

    inner class ItemViewHolder(val binding: ItemTestBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<YugiohSet>) {
        dataset = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestAdapter.ItemViewHolder {
        val binding =
            ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestAdapter.ItemViewHolder, position: Int) {

        holder.binding.tvNoteName.text = dataset[position].set_name
        holder.binding.tvNoteText.text = dataset[position].set_image

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}