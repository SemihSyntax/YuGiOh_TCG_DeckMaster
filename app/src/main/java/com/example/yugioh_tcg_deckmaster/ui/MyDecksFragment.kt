package com.example.yugioh_tcg_deckmaster.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.MainViewModel
import com.example.yugioh_tcg_deckmaster.R
import com.example.yugioh_tcg_deckmaster.adapter.MyDecksAdapter
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.databinding.FragmentMyDecksBinding
import com.example.yugioh_tcg_deckmaster.databinding.FragmentRandomCardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyDecksFragment : Fragment() {

    private lateinit var binding: FragmentMyDecksBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    private val deckList = mutableListOf<Deck>()

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

        val adapter = MyDecksAdapter()
        binding.rvMyDecks.adapter = adapter

        // RV wird nicht angezeigt?
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
            .setTitle("Deck erstellen")
            .setMessage("Gib den Namen für dein Deck ein:")
            .setView(inputField)
            .setPositiveButton("Erstellen") { _, _ ->
                val deckName = inputField.text.toString()
                // Hier wird der Code für das Hinzufügen des leeren Decks mit dem Namen ausgeführt
                val newDeck = Deck(Timestamp.now(), emptyList(), emptyList(), emptyList(), deckName, 0)
                fireBaseViewModel.addDeckToFireBase(newDeck)

                // Optional: Aktualisiere die UI, um das neue Deck anzuzeigen
                // Hier könntest du LiveData verwenden, um die UI automatisch zu aktualisieren
                // viewModel.updateDeckList(deckList)
            }
            .setNegativeButton("Abbrechen") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}