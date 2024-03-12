package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.databinding.ItemMyDecksBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
class MyDecksAdapter(private val fireBaseViewModel: FireBaseViewModel

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

        holder.binding.tvCardCountDeck.text = (deck.extraDeck.size + deck.mainDeck.size + deck.sideDeck.size).toString()
        holder.binding.tvDeckName.text = deck.name

        holder.binding.mcvDeck.setOnClickListener {
            // Setzen des ausgewählten Decks im ViewModel
            fireBaseViewModel.setSelectedDeck(deck)
            holder.itemView.findNavController().navigate(R.id.detailDeckFragment)
        }

        holder.binding.mcvDeck.setOnLongClickListener {
            // Erstellen des Dialogs zum Löschen des Decks
            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Delete deck")
                .setMessage("Are you sure you want to delete this deck?")
                .setPositiveButton("Yes") { dialog, which ->
                    fireBaseViewModel.deleteDeckFromFireBase(deck)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()

            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}