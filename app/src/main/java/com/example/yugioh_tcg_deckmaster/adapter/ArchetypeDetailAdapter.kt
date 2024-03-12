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
import com.example.yugioh_tcg_deckmaster.databinding.ItemArchetypeDetailBinding
import com.example.yugioh_tcg_deckmaster.ui.ArchetypeDetailFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ArchetypeDetailAdapter (private val viewModel : MainViewModel, private val fireBaseViewModel: FireBaseViewModel

) : RecyclerView.Adapter<ArchetypeDetailAdapter.ItemViewHolder>(){

    private var dataset = listOf<YugiohCard>()
    inner class ItemViewHolder(val binding: ItemArchetypeDetailBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<YugiohCard>) {
        dataset = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemArchetypeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val yugiohCard = dataset[position]
        holder.binding.ivArchetypeDetailCroppedImage.load(yugiohCard.card_images.first().image_url_cropped)
        holder.binding.tvArchetypeDetailCardName.text = yugiohCard.name

        holder.binding.mcvArchetypeDetail.setOnClickListener {
            // Setzen der ausgewählten Karte im ViewModel
            viewModel.selectedCard = yugiohCard
            holder.itemView.findNavController().navigate(ArchetypeDetailFragmentDirections.actionArchetypeDetailFragmentToCardDetailFragment())
        }

        holder.binding.btnArchetypeDetailAddToDeck.setOnClickListener {

            // Anzeigen eines Dialogs zur Auswahl des Decks
            showDeckSelectionDialog(holder, fireBaseViewModel.myDecks.value?: emptyList(), yugiohCard)

            Log.d("hilfe", "${fireBaseViewModel.myDecks.value}")

        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    // Funktion zum Anzeigen eines Dialogs zur Auswahl des Decks
    private fun showDeckSelectionDialog(holder: ItemViewHolder, decks: List<Deck>,card: YugiohCard) {
        val deckNames = decks.map {
            it.name
        }.toTypedArray()

        var selectedDeck: Deck? = null

        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Select deck")
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
            .setNegativeButton("Cancel", null)
            .show()
    }
}