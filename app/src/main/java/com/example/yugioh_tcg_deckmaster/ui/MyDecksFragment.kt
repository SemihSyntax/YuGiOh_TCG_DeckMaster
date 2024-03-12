package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.adapter.MyDecksAdapter
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.databinding.FragmentMyDecksBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp

class MyDecksFragment : Fragment() {

    private lateinit var binding: FragmentMyDecksBinding
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDecksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fireBaseViewModel.getDecksFromFireBase()

        val adapter = MyDecksAdapter(fireBaseViewModel)
        binding.rvMyDecks.adapter = adapter


        fireBaseViewModel.myDecks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        fireBaseViewModel.getUserName()
        fireBaseViewModel.username.observe(viewLifecycleOwner) {
            binding.tvUserDecks.text = it+"'s Decks"
        }

        binding.floatingActionButton.setOnClickListener {
            openDeckNameInputDialog()
        }

    }

    private fun openDeckNameInputDialog() {
        val inputField = EditText(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create deck")
            .setMessage("Enter the name for your deck:")
            .setView(inputField)
            .setPositiveButton("Create") { _, _ ->
                val deckName = inputField.text.toString()
                // Hier wird der Code für das Hinzufügen des leeren Decks mit dem Namen ausgeführt
                val newDeck = Deck(Timestamp.now(), emptyList(), emptyList(), emptyList(), deckName)
                fireBaseViewModel.addDeckToFireBase(newDeck)

                // Optional: Aktualisiere die UI, um das neue Deck anzuzeigen
                // Hier könntest du LiveData verwenden, um die UI automatisch zu aktualisieren
                // viewModel.updateDeckList(deckList)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}