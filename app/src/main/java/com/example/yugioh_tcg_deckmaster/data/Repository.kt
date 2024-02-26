package com.example.yugioh_tcg_deckmaster.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCardData
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSet
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi

class Repository(private val api: DeckMasterApi) {

    private val _banListTcg = MutableLiveData<List<YugiohCard>>()
    val banListTcg: LiveData<List<YugiohCard>>
        get() = _banListTcg


    private val _searchResults = MutableLiveData<List<YugiohCard>>()
    val searchResults : LiveData<List<YugiohCard>>
        get() = _searchResults


    private val _randomCard = MutableLiveData<YugiohCard>()
    val randomCard: LiveData<YugiohCard>
        get() = _randomCard


    private val _allCards = MutableLiveData<List<YugiohCard>>()
    val allCards: LiveData<List<YugiohCard>>
        get() = _allCards


    suspend fun getAllCards() {
        try {
            val response = api.retrofitService.getAllCards().data
            _allCards.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load all cards from API $e")
        }
    }
    suspend fun getBanList() {
        try {
            val response = api.retrofitService.getBanList().data
            _banListTcg.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load ban list from API $e")
        }
    }

    suspend fun searchCardByName(name: String) {
        try {
            val response = api.retrofitService.searchCardByName(name).data
            _searchResults.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load searched cards by name from API $e")
        }
    }

    suspend fun getRandomCard() {
        try {
            val response = api.retrofitService.getRandomCard()
            _randomCard.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load random card from API $e")
        }
    }

}