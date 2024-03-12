package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.FireBaseViewModel
import com.example.yugioh_tcg_deckmaster.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {


    private lateinit var binding: FragmentLogInBinding
    private val viewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachtet das errorMessage LiveData-Objekt des ViewModels und zeigt eine Toast-Nachricht an, wenn ein Fehler auftritt
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Setzt den OnClickListener für die Anmeldeschaltfläche
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etEmailUsername.text.toString(), binding.etPassword.text.toString())
        }

        // Setzt den OnClickListener für den Link zur Registrierung
        binding.tvSignUpLink.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }

    }
}