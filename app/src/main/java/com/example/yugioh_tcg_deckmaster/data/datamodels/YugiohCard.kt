package com.example.yugioh_tcg_deckmaster.data.datamodels

data class YugiohCard(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val frameType: String? = null,
    val desc: String = "",
    val atk: Int? = null,
    val def: Int? = null,
    val level: Int? = null,
    val race: String? = null,
    val attribute: String? = null,
    val archetype: String? = null,
    val scale: Int? = null,
    val linkval: String? = null,
    val linkmarkers: List<String>? = null,
    val ygoprodeck_url: String? = null,
    val card_sets: List<CardSet>? = null,
    val card_images: List<CardImage> = emptyList(),
    val banlist_info: Banlist? = null,
    val card_prices: List<CardPrice> = emptyList()
) {
    fun toHashMap(): Map<String, Any?> {
        return hashMapOf(
            "id" to id,
            "image" to card_images.first().image_url,
            "ygoprodeck_url" to ygoprodeck_url
        )
    }

    companion object {
        @JvmStatic
        fun fromMap(map: Map<String, Any?>): YugiohCard {
            return YugiohCard(
                id = (map["id"] as Number).toInt(),
                card_images = listOf(CardImage(image_url = map["image"] as String)),
                ygoprodeck_url = (map["ygoprodeck_url"] as String?)
            )
        }
    }
}


