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

    /**
     * Aktualisiert die Lebenspunkte basierend auf der eingegebenen Änderung und prüft auf einen Gewinner.
     */
    private fun updateLifePoints(isAddition: Boolean) {
        // Überprüft, ob eine der Checkboxen ausgewählt ist
        if (!binding.checkBoxPlayer1.isChecked && !binding.checkBoxPlayer2.isChecked) {
            // Zeigt einen Toast an, wenn kein Spieler ausgewählt ist
            Toast.makeText(context, "Bitte wählen Sie einen Spieler aus", Toast.LENGTH_SHORT).show()
            return
        }

        // Holt den eingegebenen Wert der Lebenspunkteänderung
        val lifePointsChange = binding.etLifePointsChange.text.toString().toIntOrNull()

        if (lifePointsChange != null) {
            // Aktualisiert die Lebenspunkte für Spieler 1, wenn dessen Checkbox ausgewählt ist
            if (binding.checkBoxPlayer1.isChecked) {
                updatePlayerLifePoints(binding.tvLifePointsPlayer1, isAddition, lifePointsChange)
            }

            // Aktualisiert die Lebenspunkte für Spieler 2, wenn dessen Checkbox ausgewählt ist
            if (binding.checkBoxPlayer2.isChecked) {
                updatePlayerLifePoints(binding.tvLifePointsPlayer2, isAddition, lifePointsChange)
            }

            // Überprüft auf einen Gewinner
            checkWinner()
        } else {
            // Behandelt ungültige Eingabe (nicht numerisch)
            Toast.makeText(context, "Bitte geben Sie eine gültige Zahl ein", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Setzt die Lebenspunkte beider Spieler auf 8000 zurück und unchecked die Ceckboxen.
     */
    private fun resetLifePoints() {
        // Setzt die Lebenspunkte beider Spieler auf 8000 zurück
        binding.tvLifePointsPlayer1.text = "8000"
        binding.tvLifePointsPlayer2.text = "8000"

        // Deaktiviert beide Checkboxen
        binding.checkBoxPlayer1.isChecked = false
        binding.checkBoxPlayer2.isChecked = false
    }

    /**
     * Überprüft, ob ein Spieler gewonnen hat.
     */
    private fun checkWinner() {
        // Überprüft, ob ein Spieler 0 Lebenspunkte erreicht hat
        val player1LifePoints = binding.tvLifePointsPlayer1.text.toString().toInt()
        val player2LifePoints = binding.tvLifePointsPlayer2.text.toString().toInt()

        if (player1LifePoints <= 0 && player2LifePoints <= 0) {
            // Beide Spieler haben 0 Lebenspunkte erreicht, es ist unentschieden
            Toast.makeText(context, "Es ist unentschieden!", Toast.LENGTH_SHORT).show()
        } else if (player1LifePoints <= 0) {
            // Spieler 2 gewinnt
            Toast.makeText(context, "Spieler 2 gewinnt!", Toast.LENGTH_SHORT).show()
        } else if (player2LifePoints <= 0) {
            // Spieler 1 gewinnt
            Toast.makeText(context, "Spieler 1 gewinnt!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Aktualisiert die Lebenspunkte eines Spielers basierend auf der Änderung.
     */
    private fun updatePlayerLifePoints(
        lifePointsTextView: TextView,
        isAddition: Boolean,
        lifePointsChange: Int
    ) {
        // Aktualisiert die Lebenspunkte basierend auf Addition oder Subtraktion
        val currentLifePoints = lifePointsTextView.text.toString().toInt()
        val newLifePoints = if (isAddition) currentLifePoints + lifePointsChange
        else currentLifePoints - lifePointsChange

        // Stellt sicher, dass die Lebenspunkte nicht unter 0 fallen
        lifePointsTextView.text = max(0, newLifePoints).toString()
    }
}