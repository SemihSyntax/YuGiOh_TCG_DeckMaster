package com.example.yugioh_tcg_deckmaster.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yugioh_tcg_deckmaster.databinding.FragmentLifePointsBinding
import kotlin.math.max

class LifePointsFragment : Fragment() {

    private lateinit var binding: FragmentLifePointsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLifePointsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClickListener for the Add button
        binding.btnAdd.setOnClickListener {
            updateLifePoints(true)
        }

        // Set onClickListener for the Subtract button
        binding.btnSubtract.setOnClickListener {
            updateLifePoints(false)
        }

        // Set onClickListener for the Reset button
        binding.btnReset.setOnClickListener {
            resetLifePoints()
        }

        binding.mtLifePointsCalculator.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateLifePoints(isAddition: Boolean) {
        // Check if either checkbox is checked
        if (!binding.checkBoxPlayer1.isChecked && !binding.checkBoxPlayer2.isChecked) {
            // Display Toast if no player is selected
            Toast.makeText(context, "Please select a player", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the entered life points change value
        val lifePointsChange = binding.etLifePointsChange.text.toString().toIntOrNull()

        if (lifePointsChange != null) {
            // Update life points for Player 1 if its CheckBox is checked
            if (binding.checkBoxPlayer1.isChecked) {
                updatePlayerLifePoints(binding.tvLifePointsPlayer1, isAddition, lifePointsChange)
            }

            // Update life points for Player 2 if its CheckBox is checked
            if (binding.checkBoxPlayer2.isChecked) {
                updatePlayerLifePoints(binding.tvLifePointsPlayer2, isAddition, lifePointsChange)
            }

            // Check for a winner
            checkWinner()
        } else {
            // Handle invalid input (non-numeric)
            Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
        }
    }


    private fun resetLifePoints() {
        // Reset life points for both players to 8000
        binding.tvLifePointsPlayer1.text = "8000"
        binding.tvLifePointsPlayer2.text = "8000"

        // Uncheck both CheckBoxes
        binding.checkBoxPlayer1.isChecked = false
        binding.checkBoxPlayer2.isChecked = false
    }

    private fun checkWinner() {
        // Check if any player has reached 0 life points
        val player1LifePoints = binding.tvLifePointsPlayer1.text.toString().toInt()
        val player2LifePoints = binding.tvLifePointsPlayer2.text.toString().toInt()

        if (player1LifePoints <= 0 && player2LifePoints <= 0) {
            // Both players reached 0 life points, it's a draw
            Toast.makeText(context, "It's a draw!", Toast.LENGTH_SHORT).show()
        } else if (player1LifePoints <= 0) {
            // Player 2 wins
            Toast.makeText(context, "Player 2 wins!", Toast.LENGTH_SHORT).show()
        } else if (player2LifePoints <= 0) {
            // Player 1 wins
            Toast.makeText(context, "Player 1 wins!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePlayerLifePoints(
        lifePointsTextView: TextView,
        isAddition: Boolean,
        lifePointsChange: Int
    ) {
        // Update life points based on addition or subtraction
        val currentLifePoints = lifePointsTextView.text.toString().toInt()
        val newLifePoints = if (isAddition) currentLifePoints + lifePointsChange
        else currentLifePoints - lifePointsChange

        // Ensure life points do not go below 0
        lifePointsTextView.text = max(0, newLifePoints).toString()
    }
}
