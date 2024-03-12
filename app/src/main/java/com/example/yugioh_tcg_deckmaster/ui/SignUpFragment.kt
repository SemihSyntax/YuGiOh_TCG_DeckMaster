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
import com.example.yugioh_tcg_deckmaster.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val fireBaseViewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt den Klick-Listener für die Registrierungsschaltfläche
        binding.btnSignUp.setOnClickListener {

            // Überprüft, ob die eingegebenen Passwörter übereinstimmen
            if (binding.etSignUpPassword.text.toString() == binding.etSignUpRepeatPassword.text.toString()) {
                // Registriert den Benutzer über den FireBaseViewModel
                fireBaseViewModel.register(
                    binding.etEmail.text.toString(),
                    binding.etSignUpPassword.text.toString(),
                    binding.etNewUsername.text.toString()
                )
            } else {
                // Zeigt eine Toast-Nachricht an, wenn die Passwörter nicht übereinstimmen
                Toast.makeText(requireContext(), "Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT).show()
            }
        }

        // Setzt den Klick-Listener für den Link zur Anmeldung
        binding.tvLoginLink.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }
    }
}