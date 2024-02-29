package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.adapter.BanlistAdapter
import com.example.yugioh_tcg_deckmaster.adapter.SearchAdapter
import com.example.yugioh_tcg_deckmaster.databinding.FragmentBanlistBinding
import com.example.yugioh_tcg_deckmaster.databinding.FragmentSearchBinding

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

        viewModel.getBanList()

        val adapter = BanlistAdapter(viewModel)
        binding.rvBanlistTcg.adapter = adapter

        viewModel.banListTcg.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.mtbBanListTCV.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }
}