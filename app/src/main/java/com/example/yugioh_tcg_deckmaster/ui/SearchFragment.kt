package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.adapter.SearchAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentSearchBinding
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Erstellt den Adapter für die Suchergebnisse
        val adapter = SearchAdapter(viewModel, fireBaseViewModel)
        binding.rvSearchResults.adapter = adapter

        // Beobachtet die Suchergebnisse und aktualisiert den Adapter entsprechend
        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            searchResults?.let {
                adapter.submitList(it)
            }
        }

        // Setzt den Text-Listener für die Sucheingabe
        binding.editTextSearch.addTextChangedListener { editable ->
            editable?.let {
                viewModel.searchCard(it.toString())
            }
        }
    }
}