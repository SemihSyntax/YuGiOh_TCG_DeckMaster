package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCardData
import com.example.yugioh_tcg_deckmaster.databinding.ItemMyDecksBinding
import com.example.yugioh_tcg_deckmaster.databinding.ItemSearchBinding
import com.example.yugioh_tcg_deckmaster.ui.SearchFragmentDirections

class MyDecksAdapter(

) : RecyclerView.Adapter<MyDecksAdapter.ItemViewHolder>(){

    private var dataset = listOf<Deck>()
    inner class ItemViewHolder(val binding: ItemMyDecksBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Deck>) {
        dataset = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemMyDecksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val deck = dataset[position]

        holder.binding.tvCardCountDeck.text = deck.cardCount.toString()
        holder.binding.tvDeckName.text = deck.name

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}