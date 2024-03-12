package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.databinding.ItemBanlistBinding
import com.example.yugioh_tcg_deckmaster.ui.BanlistFragmentDirections

class BanlistAdapter(private val viewModel: MainViewModel

) : RecyclerView.Adapter<BanlistAdapter.ItemViewHolder>(){

    private var dataset = listOf<YugiohCard>()
    inner class ItemViewHolder(val binding: ItemBanlistBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<YugiohCard>) {
        dataset = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemBanlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val card = dataset[position]

        holder.binding.tvBannedCardName.text = card.name
        holder.binding.tvBanStyle.text = card.banlist_info?.ban_tcg ?: "error"
        holder.binding.ivBannedCardImage.load(card.card_images.first().image_url_cropped)

        holder.binding.mcvBanlist.setOnClickListener {
            viewModel.selectedCard = card
            holder.itemView.findNavController().navigate(BanlistFragmentDirections.actionBanlistFragmentToCardDetailFragment())
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}