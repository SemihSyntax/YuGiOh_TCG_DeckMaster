package com.example.yugioh_tcg_deckmaster

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.yugioh_tcg_deckmaster.data.Repository
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCardData
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSetData
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(DeckMasterApi)



    val banListTcg = repository.banListTcg

    val searchResults = repository.searchResults

    val randomCard = repository.randomCard

    val allCards = repository.allCards



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
            } catch (e:Exception) {
                Log.e("MVM", "$e")
            }
        }
    }

    fun searchCardByName(name: String) {
        viewModelScope.launch {
            try {
                repository.searchCardByName(name)
            } catch (e: Exception) {
                Log.e("MVM", "$e")
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