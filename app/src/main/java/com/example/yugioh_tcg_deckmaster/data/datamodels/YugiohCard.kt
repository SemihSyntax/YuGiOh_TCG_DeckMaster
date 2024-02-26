package com.example.yugioh_tcg_deckmaster.data.datamodels

data class YugiohCard(
    val id: Int,
    val name: String,
    val type: String,
    val frameType: String? = null,
    val desc: String,
    val atk: Int? = null,
    val def: Int? = null,
    val level: Int? = null,
    val race: String? = null,
    val attribute: String? = null,
    val archetype: String? = null,
    val scale: Int? = null,
    val linkval: String? = null,
    val linkmarkers: List<String>? = null,
    val card_sets: List<CardSet>? = null,
    val card_images: List<CardImage>,
    val banlist_info: Banlist? = null,
    val card_prices: List<CardPrice>
)
