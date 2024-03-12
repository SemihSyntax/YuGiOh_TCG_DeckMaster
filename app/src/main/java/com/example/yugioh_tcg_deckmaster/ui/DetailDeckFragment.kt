package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.adapter.DeckDetailAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentDetailDeckBinding
class DetailDeckFragment : Fragment() {

    private lateinit var binding: FragmentDetailDeckBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailDeckBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachtet das ausgewählte Deck im FireBaseViewModel
        fireBaseViewModel.selectedDeck.observe(viewLifecycleOwner) {deck ->

            // Erstellt Adapter für das Hauptdeck, das Extra-Deck und das Side-Deck
            val adapterMainDeck = DeckDetailAdapter(viewModel, fireBaseViewModel, deck!!)
            val adapterExtraDeck = DeckDetailAdapter(viewModel, fireBaseViewModel, deck!!)
            val adapterSideDeck = DeckDetailAdapter(viewModel, fireBaseViewModel, deck!!)

            // Setzt die Adapter für die entsprechenden RecyclerViews
            binding.recyclerViewMainDeck.adapter = adapterMainDeck
            binding.recyclerViewExtraDeck.adapter = adapterExtraDeck
            binding.recyclerViewSideDeck.adapter = adapterSideDeck

            // Überprüft, ob das Deck nicht null ist, und reicht die Liste der Karten an die Adapter weiter
            if (deck != null) {
                adapterMainDeck.submitList(deck.mainDeck)
                adapterExtraDeck.submitList(deck.extraDeck)
                adapterSideDeck.submitList(deck.sideDeck)
            }
            binding.mtbDetailDeck.title = deck?.name
        }


//        binding.expandMainDeck.setOnClickListener {
//            binding.recyclerViewMainDeck.visibility = View.VISIBLE
//        }

        binding.mtbDetailDeck.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


    }
}