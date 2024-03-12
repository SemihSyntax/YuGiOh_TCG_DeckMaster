package com.example.yugioh_tcg_deckmaster.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yugioh_tcg_deckmaster.data.datamodels.Archetype
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi

class Repository(private val api: DeckMasterApi) {

    // LiveData für die Banliste der TCG-Version
    private val _banListTcg = MutableLiveData<List<YugiohCard>>()
    val banListTcg: LiveData<List<YugiohCard>>
        get() = _banListTcg

    // LiveData für die Suchergebnisse
    private val _searchResults = MutableLiveData<List<YugiohCard>?>()
    val searchResults : LiveData<List<YugiohCard>?>
        get() = _searchResults

    // LiveData für eine zufällige Karte
    private val _randomCard = MutableLiveData<YugiohCard>()
    val randomCard: LiveData<YugiohCard>
        get() = _randomCard

    // LiveData für alle verfügbaren Karten
    private val _allCards = MutableLiveData<List<YugiohCard>?>()
    val allCards: LiveData<List<YugiohCard>?>
        get() = _allCards

    // LiveData für alle Archetypen
    private val _allArchetypes = MutableLiveData<List<Archetype>>()
    val allArchetypes: LiveData<List<Archetype>>
        get() = _allArchetypes

    // LiveData für Karten nach Archetyp
    private val _cardsByArchetype = MutableLiveData<List<YugiohCard>>()
    val cardsbyArchetype: LiveData<List<YugiohCard>>
        get() = _cardsByArchetype

    /**
     * Ruft alle Archetypen von der API ab und aktualisiert den entsprechenden LiveData.
     */
    suspend fun getAllArchetypes() {
        try {
            val response = api.retrofitService.getAllArchetypes()
            _allArchetypes.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load all archetypes from API $e")
        }
    }

    /**
     * Ruft alle verfügbaren Karten von der API ab und aktualisiert den entsprechenden LiveData.
     */
    suspend fun getAllCards() {
        try {
            val response = api.retrofitService.getAllCards().data
            _allCards.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load all cards from API $e")
        }
    }

    /**
     * Ruft die Banliste von der API ab und aktualisiert den entsprechenden LiveData.
     */
    suspend fun getBanList() {
        try {
            val response = api.retrofitService.getBanList().data
            _banListTcg.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load banlist from API $e")
        }
    }

    /**
     * Entfernt Sonderzeichen und Leerzeichen aus dem Kartennamen.
     *
     * @param name Der zu säubernde Kartennamen.
     * @return Der gesäuberte Kartennamen.
     */
    private fun sanitizeCardName(name: String): String {
        return name.replace(Regex("[^a-z0-9]"), "")
    }

    /**
     * Sucht nach einer Karte anhand ihres Namens und aktualisiert den entsprechenden LiveData mit den Suchergebnissen.
     *
     * @param name Der Name der zu suchenden Karte.
     */
    suspend fun searchCard(name: String) {
        val sanitizedName = sanitizeCardName(name.lowercase())

        val filteredCards = _allCards.value?.filter {
            sanitizeCardName(it.name.lowercase()).contains(sanitizedName)
        }

        _searchResults.postValue(filteredCards)
    }

    /**
     * Ruft eine zufällige Karte von der API ab und aktualisiert den entsprechenden LiveData.
     */
    suspend fun getRandomCard() {
        try {
            val response = api.retrofitService.getRandomCard()
            _randomCard.postValue(api.retrofitService.getCardById(response.id).data.first())
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load random card from API $e")
        }
    }

    /**
     * Ruft alle Karten eines Archetyps von der API ab und aktualisiert den entsprechenden LiveData.
     *
     * @param archetype Der Name des Archetyps.
     */
    suspend fun getCardsByArchetype(archetype: String) {
        try {
            val response = api.retrofitService.getCardsByArchetype(archetype).data
            _cardsByArchetype.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo","Failed to load cards by archetype from API $e")
        }
    }
}