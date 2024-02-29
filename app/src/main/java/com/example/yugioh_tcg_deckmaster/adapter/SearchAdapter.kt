package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.databinding.ItemSearchBinding
import com.example.yugioh_tcg_deckmaster.ui.SearchFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchAdapter (private val viewModel : MainViewModel, private val fireBaseViewModel: FireBaseViewModel

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

        holder.binding.mcvSearchCard.setOnClickListener {
            viewModel.selectedCard = yugiohCard
            holder.itemView.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCardDetailFragment())
        }

        holder.binding.btnAddToDeck.setOnClickListener {

            showDeckSelectionDialog(holder, fireBaseViewModel.myDecks.value?: emptyList(), yugiohCard)

            Log.d("hilfe", "${fireBaseViewModel.myDecks.value}")

        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    private fun showDeckSelectionDialog(holder: ItemViewHolder, decks: List<Deck>,card: YugiohCard) {
        val deckNames = decks.map {
            it.name
        }.toTypedArray()

        var selectedDeck: Deck? = null

        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Deck auswählen")
            .setSingleChoiceItems(deckNames, -1) { dialog, which ->
                // Hier wird der ausgewählte Index (which) oder der ausgewählte Deck-Name verwendet
                selectedDeck = decks[which]
                // Mach etwas mit dem ausgewählten Deck

            }
            // Hier kannst du eine Aktion beim Bestätigen hinzufügen
            .setPositiveButton("OK") { _, _ ->
                val mainDeck = selectedDeck?.mainDeck
                val newMainDeck = mainDeck?.plus(card)
                if (newMainDeck != null) {
                    selectedDeck?.mainDeck = newMainDeck
                }
                selectedDeck?.let {
                    fireBaseViewModel.updateDeckInFirebase(selectedDeck?.timeStamp.toString(),
                        it
                    )
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }
}