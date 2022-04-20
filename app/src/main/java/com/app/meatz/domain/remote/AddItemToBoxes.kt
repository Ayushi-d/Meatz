package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class AddItemToBoxes(
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("items_count")
        val itemsCount: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("total")
        val total: String
)