package com.app.meatz.domain.remote.generalResponse


import com.google.gson.annotations.SerializedName

data class Banner(
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