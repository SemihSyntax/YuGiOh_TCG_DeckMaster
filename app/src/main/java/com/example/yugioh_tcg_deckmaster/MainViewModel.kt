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


    init {
        getAllCards()
    }

    val banListTcg = repository.banListTcg

    val searchResults = repository.searchResults

    val randomCard = repository.randomCard

    var selectedCard: YugiohCard? = null

    val allArchetypes = repository.allArchetypes

    val cardsByArchetype = repository.cardsbyArchetype


    fun getAllArchetypes() {
        viewModelScope.launch {
            try {
                repository.getAllArchetypes()
            } catch (e: Exception) {
                Log.e("MVM","$e")
            }
        }
    }

    fun getCardsByArchetype(archetype: String) {
        viewModelScope.launch {
            try {
                repository.getCardsByArchetype(archetype)
            } catch (e: Exception) {
                Log.e("MVM", "$e")
            }
        }
    }


    fun getAllCards() {
        viewModelScope.launch {
            try {
                repository.getAllCards()
            } catch (e: Exception) {
                Log.e("MVM", "$e")
            }
        }
    }

    fun getBanList() {
        viewModelScope.launch {
            try {
                repository.getBanList()
            } catch (e: Exception) {
                Log.e("MVM", "$e")
            }
        }
    }

    fun searchCard(name: String) {
        viewModelScope.launch {
            try {
                repository.searchCard(name)
            } catch (e:Exception) {
                Log.e("MVM","$e")
            }
        }
    }

    fun getRandomCard() {
        viewModelScope.launch {
            try {
                repository.getRandomCard()
            } catch (e: Exception) {
                Log.e("MWM", "$e")
            }
        }
    }
}