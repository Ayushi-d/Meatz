package com.app.meatz.domain.remote


import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class OfferBoxDetails(
        @SerializedName("content")
        val content: List<String>,
        @SerializedName("id")
        val id: Int,
        @SerializedName("images")
        val images: List<String>,
        @SerializedName("name")
        val name: String,
        @SerializedName("persons")
        val persons: Int,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: String? = "0.000",
        @SerializedName("stocks")
        val stocks: Int,
        @SerializedName("store")
        val store: StoreData?
)