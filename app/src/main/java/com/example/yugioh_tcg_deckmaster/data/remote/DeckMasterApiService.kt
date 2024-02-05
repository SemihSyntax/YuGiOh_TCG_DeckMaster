package com.example.yugioh_tcg_deckmaster.data.remote

import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCardData
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohSetData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit  = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DeckMasterApiService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): YugiohCardData

    @GET("randomcard.php")
    suspend fun getRandomCard(): YugiohCard

    @GET("cardsets.php")
    suspend fun getAllCardSets(): YugiohSetData

}

object DeckMasterApi {
    val retrofitService: DeckMasterApiService by lazy { retrofit.create(DeckMasterApiService::class.java) }
}