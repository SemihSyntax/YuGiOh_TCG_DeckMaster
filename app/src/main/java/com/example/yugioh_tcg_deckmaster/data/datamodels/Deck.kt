package com.example.yugioh_tcg_deckmaster.data.datamodels

import com.google.firebase.Timestamp

data class Deck(
    var timeStamp: Timestamp,
    var mainDeck: List<YugiohCard>,
    var extraDeck: List<YugiohCard>,
    var sideDeck: List<YugiohCard>,
    var name: String
) {

    // Methode zum Konvertieren des Decks in eine HashMap (zum abspeichern in FireBase)
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "timeStamp" to timeStamp,
            "mainDeck" to mainDeck.map { it.toHashMap() }, // Wandelt jede YugiohCard in eine HashMap um
            "extraDeck" to extraDeck.map { it.toHashMap() },
            "sideDeck" to sideDeck.map { it.toHashMap() },
            "name" to name
        )
    }

    // Statische Methode zum Erstellen eines Deck-Objekts aus einer Map (aus FireBase)
    companion object {
        fun fromMap(map: Map<String, Any>): Deck {
            val timeStamp = map["timeStamp"] as Timestamp
            val mainDeck =
                (map["mainDeck"] as List<Map<String, Any>>).map { YugiohCard.fromMap(it) }
            val extraDeck =
                (map["extraDeck"] as List<Map<String, Any>>).map { YugiohCard.fromMap(it) }
            val sideDeck =
                (map["sideDeck"] as List<Map<String, Any>>).map { YugiohCard.fromMap(it) }
            val name = map["name"] as String
            return Deck(timeStamp, mainDeck, extraDeck, sideDeck, name)
        }
    }
}

