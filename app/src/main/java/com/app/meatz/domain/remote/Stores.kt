package com.app.meatz.domain.remote.stores


import com.app.meatz.domain.remote.generalResponse.Cart
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class Stores(
        @SerializedName("ads")
        val ads: List<Ad>,
        @SerializedName("stores")
        val storeData: List<StoreData>,
        @SerializedName("cart")
        val cart: Cart
)


data class Ad(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("model")
        val model: String,
        @SerializedName("model_id")
        val modelId: Int,
        @SerializedName("sort")
        val sort: Int,
        @SerializedName("status")
        val status: Int,
        @SerializedName("title")
        val title: String
)