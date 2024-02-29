package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.adapter.ArchetypeAdapter
import com.example.yugioh_tcg_deckmaster.adapter.BanlistAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentArchetypeBinding
import com.example.yugioh_tcg_deckmaster.databinding.FragmentBanlistBinding

class ArchetypeFragment : Fragment() {

    private lateinit var binding: FragmentArchetypeBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchetypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllArchetypes()

        val adapter = ArchetypeAdapter()
        binding.rvAllArchetypes.adapter = adapter

        viewModel.allArchetypes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.mtbAllArchetypes.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}