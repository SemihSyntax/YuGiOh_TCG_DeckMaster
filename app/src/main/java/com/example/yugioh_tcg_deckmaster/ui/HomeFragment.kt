package com.example.yugioh_tcg_deckmaster.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.databinding.FragmentHomeBinding
import com.example.yugioh_tcg_deckmaster.databinding.FragmentRandomCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

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

        viewModel.getRandomCard()

        viewModel.randomCard.observe(viewLifecycleOwner) {
            binding.ivHomeRandomCard.load(it.card_images.first().image_url)
            Log.d("test1", "${it.ygoprodeck_url}")
            Log.d("test2", "$it")
        }

        // umändern zu navigate profile fragment und dort ausloggen können
        binding.mtbHomeFragment.setOnMenuItemClickListener {
            fireBaseViewModel.logout()
            true
        }

        binding.ivHomeRandomCard.setOnClickListener {
            viewModel.selectedCard = viewModel.randomCard.value
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCardDetailFragment())
        }

        binding.btnTcgBanList.setOnClickListener {
            findNavController().navigate(R.id.banlistFragment)
        }

        binding.btnCalculator.setOnClickListener {
            findNavController().navigate(R.id.lifePointsFragment)
        }

        binding.btnArchetypes.setOnClickListener {
            findNavController().navigate(R.id.archetypeFragment)
        }
    }
}


