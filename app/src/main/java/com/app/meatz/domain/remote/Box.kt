package com.app.meatz.domain.remote.box


import com.app.meatz.domain.remote.generalResponse.Cart
import com.google.gson.annotations.SerializedName

data class Box(
        @SerializedName("boxes")
        val boxes: List<BoxData>,
        @SerializedName("cart")
        val cart: Cart
)


data class BoxData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("persons")
        val persons: Int,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: Double = 0.0,
        @SerializedName("stocks")
        val stocks: Int
)

