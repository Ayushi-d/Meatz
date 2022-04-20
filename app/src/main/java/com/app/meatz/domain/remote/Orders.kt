package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Orders(
        @SerializedName("can_reorder")
        val canReorder: Int,
        @SerializedName("code")
        val code: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("total")
        val total: String,
        @SerializedName("items_count")
        val items_count: Int
)