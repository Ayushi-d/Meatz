package com.app.meatz.domain.remote.generalResponse

import com.app.meatz.domain.remote.CartProductOption
import com.google.gson.annotations.SerializedName

data class ProductData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("cart_id")
        val cart_id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: Double = 0.0,
        @SerializedName("type")
        val type: String = "",
        @SerializedName("num")
        val num: Int,
        @SerializedName("count")
        var count: Int,
        @SerializedName("persons")
        val persons: Int = 0,
        @SerializedName("option_items")
        val options: List<ProductOptionItems>,
        @SerializedName("store")
        val store: StoreData?

)