package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.adapter.ArchetypeAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentArchetypeBinding

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

        // Ruft alle Kartentypen ab
        viewModel.getAllArchetypes()

        // Erstellt einen Adapter für die Anzeige der Kartentypen
        val adapter = ArchetypeAdapter()
        binding.rvAllArchetypes.adapter = adapter

        // Beobachtet die Änderungen an der Liste der Kartentypen und aktualisiert den Adapter
        viewModel.allArchetypes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Setzt den Klick-Listener für das Navigations-Icon
        binding.mtbAllArchetypes.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}