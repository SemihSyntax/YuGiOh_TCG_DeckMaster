package com.example.yugioh_tcg_deckmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yugioh_tcg_deckmaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)


        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                // die IDs der Fragemnte hinzufügen in welcher die BotNav ausgeblendet werden soll

                //R.id.chatDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                //R.id.statusDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val viewModel = ViewModelProvider(this)[FireBaseViewModel::class.java]
        val navController = navHost.navController

        viewModel.user.observe(this) {
            if (it == null) {
                navController.navigate(R.id.logInFragment)
            } else {
                navController.navigate(R.id.homeFragment)
            }
        }



    }
}