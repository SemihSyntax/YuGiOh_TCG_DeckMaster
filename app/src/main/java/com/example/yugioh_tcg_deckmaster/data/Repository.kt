package com.example.yugioh_tcg_deckmaster.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSet
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi
import java.lang.Exception

class Repository(private val api: DeckMasterApi) {

    private val _yugiohSet = MutableLiveData<List<YugiohSet>>()
    val yugiohSet: LiveData<List<YugiohSet>>
        get() = _yugiohSet

    private val _cards Live<List<YugiohCard>>

    private val _cardMonster

    private val cardSpellTrap

    suspend fun loadSets(){
        try {
            val response = api.retrofitService.getAllCardSets().sets
            _yugiohSet.postValue(response)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to load Sets from API $e")
        }
    }

}