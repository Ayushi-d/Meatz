package com.app.meatz.domain.remote


import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class MyBoxDetails(
        @SerializedName("box")
        val box: Box,
        @SerializedName("products")
        val boxProducts: List<BoxProducts>
)


data class Box(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("user_id")
        val userId: Int
)

data class BoxProducts(
        @SerializedName("count")
        val count: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: Double = 0.0,
        @SerializedName("store")
        val store: StoreData,
        @SerializedName("total")
        val total: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("options")
        val options: List<Options>
)

data class Options(
        @SerializedName("name")
        val name: String,
        @SerializedName("pivot")
        val pivot: Pivot,
        @SerializedName("price")
        val price: String
)

data class Pivot(
        @SerializedName("max")
        val max: Int,
        @SerializedName("min")
        val min: Int,
        @SerializedName("option_id")
        val optionId: Int,
        @SerializedName("product_id")
        val productId: Int
)