package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.databinding.FragmentCardDetailBinding

class CardDetailFragment : Fragment() {

    private lateinit var binding: FragmentCardDetailBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mtDetailCard.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.wvDetailCard.webViewClient = WebViewClient()
        binding.wvDetailCard.loadUrl(viewModel.selectedCard?.ygoprodeck_url.toString())
    }
}
