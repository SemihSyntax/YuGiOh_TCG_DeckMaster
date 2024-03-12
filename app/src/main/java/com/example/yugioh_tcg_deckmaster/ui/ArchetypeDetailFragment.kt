package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.adapter.ArchetypeDetailAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentArchetypeDetailBinding
class ArchetypeDetailFragment : Fragment() {

    private lateinit var binding: FragmentArchetypeDetailBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    private val args: ArchetypeDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchetypeDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ruft die Karten für den angegebenen Archetyp ab
        viewModel.getCardsByArchetype(args.archetype)

        val adapter = ArchetypeDetailAdapter(viewModel,fireBaseViewModel)
        binding.rvArchetypeDetail.adapter = adapter

        viewModel.cardsByArchetype.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.mtbArchetypeDetail.title = args.archetype

        binding.mtbArchetypeDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}