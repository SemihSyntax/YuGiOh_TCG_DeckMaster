package com.example.yugioh_tcg_deckmaster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.yugioh_tcg_deckmaster.data.Repository
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSetData
import com.example.yugioh_tcg_deckmaster.data.remote.DeckMasterApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(DeckMasterApi)

    private val _sets = repository.yugiohSet
    val sets: LiveData<YugiohSetData>
        get() = _sets

    fun loadSets() {
        viewModelScope.launch {
            repository.loadSets()
        }
    }
}