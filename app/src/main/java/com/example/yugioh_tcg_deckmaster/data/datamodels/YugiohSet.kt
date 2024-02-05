package com.example.yugioh_tcg_deckmaster.data.datamodels

data class YugiohSet(
    val set_name: String,
    val set_code: String,
    val num_of_cards: Int,
    val tcg_date: String,
    val set_image: String? = null
)
