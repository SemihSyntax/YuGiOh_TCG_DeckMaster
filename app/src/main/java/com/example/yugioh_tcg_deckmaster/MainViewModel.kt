package com.example.yugioh_tcg_deckmaster

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yugioh_tcg_deckmaster.data.Repository
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(DeckMasterApi)

    // Initialisierung, um beim Starten der App alle Karten abzurufen
    init {
        getAllCards()
    }

    // LiveData-Objekte, die von Repository abgerufen werden

    val banListTcg = repository.banListTcg

    val searchResults = repository.searchResults

    val randomCard = repository.randomCard

    val allArchetypes = repository.allArchetypes

    val cardsByArchetype = repository.cardsbyArchetype

    // Ausgewählte Karte für den Detailbildschirm
    var selectedCard: YugiohCard? = null


    /**
     * Methode zum Abrufen aller Kartensätze vom Repository.
     */
    fun getAllCards() {
        viewModelScope.launch {
            try {
                repository.getAllCards()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    /**
     * Methode zum Abrufen der Sperrliste vom Repository.
     */
    fun getBanList() {
        viewModelScope.launch {
            try {
                repository.getBanList()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    /**
     * Methode zum Suchen einer Karte nach Namen.
     */
    fun searchCard(name: String) {
        viewModelScope.launch {
            try {
                repository.searchCard(name)
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    /**
     * Methode zum Abrufen einer zufälligen Karte vom Repository.
     */
    fun getRandomCard() {
        viewModelScope.launch {
            try {
                repository.getRandomCard()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    /**
     * Methode zum Abrufen aller Archetypen vom Repository.
     */
    fun getAllArchetypes() {
        viewModelScope.launch {
            try {
                repository.getAllArchetypes()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    /**
     * Methode zum Abrufen von Karten eines bestimmten Archetyps vom Repository.
     */
    fun getCardsByArchetype(archetype: String) {
        viewModelScope.launch {
            try {
                repository.getCardsByArchetype(archetype)
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }
}