package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.data.datamodels.Archetype
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.databinding.ItemArchetypeBinding
import com.example.yugioh_tcg_deckmaster.databinding.ItemDeckDetailBinding

class DeckDetailAdapter(

) : RecyclerView.Adapter<DeckDetailAdapter.ItemViewHolder>(){

    private var dataset = listOf<YugiohCard>()
    inner class ItemViewHolder(val binding: ItemDeckDetailBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<YugiohCard>) {
        dataset = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemDeckDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val yugiohCard = dataset[position]

        holder.binding.ivItemDeckCard.load(yugiohCard.card_images.first().image_url)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}