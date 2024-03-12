package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ruft eine zufällige Karte vom ViewModel ab
        viewModel.getRandomCard()


        // Beobachtet die zufällige Karte und lädt das Bild in die ImageView, wenn eine neue Karte verfügbar ist
        viewModel.randomCard.observe(viewLifecycleOwner) {
            binding.ivHomeRandomCard.load(it.card_images.first().image_url)
        }

        // Setzt den OnClickListener für das Menü in der Toolbar, um zum Profilfragment zu navigieren
        binding.mtbHomeFragment.setOnMenuItemClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment2())
            true
        }

        // Setzt den OnClickListener für die zufällige Karte, um zum Detailfragment der ausgewählten Karte zu navigieren
        binding.ivHomeRandomCard.setOnClickListener {
            viewModel.selectedCard = viewModel.randomCard.value
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCardDetailFragment())
        }

        // Setzt den OnClickListener für den Button "TCG Ban List", um zum Banlistfragment zu navigieren
        binding.btnTcgBanList.setOnClickListener {
            findNavController().navigate(R.id.banlistFragment)
        }

        // Setzt den OnClickListener für den Button "Calculator", um zum LifePointsFragment zu navigieren
        binding.btnCalculator.setOnClickListener {
            findNavController().navigate(R.id.lifePointsFragment)
        }

        // Setzt den OnClickListener für den Button "Archetypes", um zum ArchetypeFragment zu navigieren
        binding.btnArchetypes.setOnClickListener {
            findNavController().navigate(R.id.archetypeFragment)
        }
    }
}


