package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.adapter.BanlistAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentBanlistBinding

class BanlistFragment : Fragment() {

    private lateinit var binding: FragmentBanlistBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBanlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ruft die Verbotenen- und Limitierten-Liste ab
        viewModel.getBanList()

        // Erstellt einen Adapter für die Anzeige der Verbotenen- und Limitierten-Liste
        val adapter = BanlistAdapter(viewModel)
        binding.rvBanlistTcg.adapter = adapter

        // Beobachtet die Änderungen an der Verbotenen- und Limitierten-Liste und aktualisiert den Adapter
        viewModel.banListTcg.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Setzt den Klick-Listener für das Navigations-Icon
        binding.mtbBanListTCV.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}