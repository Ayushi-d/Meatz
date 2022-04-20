package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class BoxDetails(
        @SerializedName("content")
        val content: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("images")
        val images: List<Image>,
        @SerializedName("liked")
        val liked: Int = 0,
        @SerializedName("name")
        val name: String,
        @SerializedName("options")
        val options: List<Any>,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val priceBefore: Double = 0.0,
        @SerializedName("persons")
        val persons: Int = 0,
        @SerializedName("num")
        val num: Int
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