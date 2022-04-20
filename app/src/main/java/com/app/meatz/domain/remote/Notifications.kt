package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Notifications(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("model")
        val model: String,
        @SerializedName("model_id")
        val modelId: Int = 0,
        @SerializedName("text")
        val text: String
)