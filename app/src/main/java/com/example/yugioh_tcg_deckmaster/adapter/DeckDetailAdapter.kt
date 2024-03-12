package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.databinding.ItemDeckDetailBinding
import com.example.yugioh_tcg_deckmaster.ui.DetailDeckFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeckDetailAdapter(private val viewModel: MainViewModel, private val fireBaseViewModel: FireBaseViewModel, private val deck: Deck

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

        holder.binding.ivItemDeckCard.setOnClickListener {
            // Setzen der ausgewählten Karte im ViewModel
            viewModel.selectedCard = yugiohCard
            holder.itemView.findNavController().navigate(DetailDeckFragmentDirections.actionDetailDeckFragmentToCardDetailFragment())
        }


        holder.binding.ivItemDeckCard.setOnLongClickListener {
            // Erstellen des Dialogs zum Löschen der Karte aus dem Deck
            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Delete Card")
                .setMessage("Do you want to remove this card from the deck?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // User clicked "Yes"
                    fireBaseViewModel.deleteCardFromDeckInFireBase(yugiohCard, deck)
                    dialog.dismiss() // Dismiss the dialog
                }
                .setNegativeButton("No") { dialog, _ ->
                    // User clicked "No"
                    dialog.dismiss() // Dismiss the dialog
                }
                .show()

            true // Event handled
        }



    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}