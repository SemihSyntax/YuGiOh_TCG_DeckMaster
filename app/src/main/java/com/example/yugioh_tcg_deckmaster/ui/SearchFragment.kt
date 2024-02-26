package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.adapter.SearchAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentRandomCardBinding
import com.example.yugioh_tcg_deckmaster.databinding.FragmentSearchBinding
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MainViewModel by activityViewModels()

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

        val adapter = SearchAdapter()
        binding.rvSearchResults.adapter = adapter

        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.editTextSearch.addTextChangedListener {
            viewModel.searchCardByName(it.toString())
        }
    }
}