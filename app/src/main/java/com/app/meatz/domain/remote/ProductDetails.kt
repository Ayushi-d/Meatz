package com.app.meatz.domain.remote.productDetails


import com.app.meatz.domain.remote.generalResponse.Option
import com.google.gson.annotations.SerializedName

data class ProductDetails(
        @SerializedName("content")
        val content: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("images")
        val images: List<Image>,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("liked")
        val liked: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("num")
        val num: Int,
        @SerializedName("options")
        val options: List<Option>,
        @SerializedName("persons")
        val persons: Any,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: Double = 0.0,
        @SerializedName("type")
        val type: String
)

data class Image(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("imageable_id")
        val imageableId: Int,
        @SerializedName("imageable_type")
        val imageableType: String,
        @SerializedName("updated_at")
        val updatedAt: String
)