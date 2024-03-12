package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt den Klick-Listener für die Logout-Schaltfläche
        binding.buttonLogOut.setOnClickListener {
            fireBaseViewModel.logout()
        }

        // Setzt den Klick-Listener für das Navigations-Icon
        binding.mtProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Zeigt die E-Mail-Adresse des Benutzers an
        binding.textViewEmail.text = fireBaseViewModel.auth.currentUser?.email

        // Ruft den Benutzernamen ab und aktualisiert die UI
        fireBaseViewModel.getUserName()
        fireBaseViewModel.username.observe(viewLifecycleOwner) {
            binding.textViewUsername.text = it
        }
    }
}