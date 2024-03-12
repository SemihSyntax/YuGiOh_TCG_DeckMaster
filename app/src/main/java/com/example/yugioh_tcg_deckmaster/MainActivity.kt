package com.example.yugioh_tcg_deckmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yugioh_tcg_deckmaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktivität initialisieren und Layout binden
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation Host Fragment finden und mit dem NavController verknüpfen
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        // Überwache Änderungen im NavController und passe die Sichtbarkeit der Bottom Navigation an
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cardDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        // Viewmodel für Firebase-Authentifizierung initialisieren
        val viewModel = ViewModelProvider(this)[FireBaseViewModel::class.java]
        val navController = navHost.navController

        // Überprüfe den aktuellen Benutzerstatus und navigiere entsprechend
        viewModel.user.observe(this) {
            if (it == null) {
                navController.navigate(R.id.logInFragment)
            } else {
                navController.navigate(R.id.homeFragment)
            }
        }
    }
}