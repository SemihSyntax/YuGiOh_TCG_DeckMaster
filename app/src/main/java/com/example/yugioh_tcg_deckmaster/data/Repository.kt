package com.example.yugioh_tcg_deckmaster.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yugioh_tcg_deckmaster.data.datamodels.Archetype
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi

class Repository(private val api: DeckMasterApi) {

    private val _banListTcg = MutableLiveData<List<YugiohCard>>()
    val banListTcg: LiveData<List<YugiohCard>>
        get() = _banListTcg


    private val _searchResults = MutableLiveData<List<YugiohCard>?>()
    val searchResults : LiveData<List<YugiohCard>?>
        get() = _searchResults


    private val _randomCard = MutableLiveData<YugiohCard>()
    val randomCard: LiveData<YugiohCard>
        get() = _randomCard


    private val _allCards = MutableLiveData<List<YugiohCard>?>()
    val allCards: LiveData<List<YugiohCard>?>
        get() = _allCards

    private val _allArchetypes = MutableLiveData<List<Archetype>>()
    val allArchetypes: LiveData<List<Archetype>>
        get() = _allArchetypes

    private val _cardsByArchetype = MutableLiveData<List<YugiohCard>>()
    val cardsbyArchetype: LiveData<List<YugiohCard>>
        get() = _cardsByArchetype


    suspend fun getAllArchetypes() {
        try {
            val response = api.retrofitService.getAllArchetypes()
            _allArchetypes.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load all archetypes from API $e")
        }
    }


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

    private fun sanitizeCardName(name: String): String {
        // Entferne Leerzeichen und Sonderzeichen aus dem Namen
        return name.replace(Regex("[^a-z0-9]"), "")
    }

    suspend fun searchCard(name: String) {
        val sanitizedName = sanitizeCardName(name.lowercase())

        val filteredCards = _allCards.value?.filter {
            sanitizeCardName(it.name.lowercase()).contains(sanitizedName)
        }

        _searchResults.postValue(filteredCards)
    }

    suspend fun getRandomCard() {
        try {
            val response = api.retrofitService.getRandomCard()
            _randomCard.postValue(api.retrofitService.getCardById(response.id).data.first())
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load random card from API $e")
        }
    }

    suspend fun getCardsByArchetype(archetype: String) {
        try {
            val response = api.retrofitService.getCardsByArchetype(archetype).data
            _cardsByArchetype.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load cards by archetype from API $e")
        }
    }

}