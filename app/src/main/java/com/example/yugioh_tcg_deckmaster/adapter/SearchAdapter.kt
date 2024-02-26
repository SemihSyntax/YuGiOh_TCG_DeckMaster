package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.databinding.ItemSearchBinding

class SearchAdapter (

) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>(){

    private var dataset = listOf<YugiohCard>()
    inner class ItemViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<YugiohCard>) {
        dataset = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val yugiohCard = dataset[position]
        holder.binding.ivSearchCroppedImage.load(yugiohCard.card_images.first().image_url_cropped)
        holder.binding.textViewCardName.text = yugiohCard.name
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}