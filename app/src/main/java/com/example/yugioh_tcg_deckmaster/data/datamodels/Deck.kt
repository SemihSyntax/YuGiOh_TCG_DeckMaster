package com.example.yugioh_tcg_deckmaster.data.datamodels

import com.google.firebase.Timestamp

data class Deck(
    var timeStamp: Timestamp,
    var mainDeck: List<YugiohCard>,
    var extraDeck: List<YugiohCard>,
    var sideDeck: List<YugiohCard>,
    var name: String
)
