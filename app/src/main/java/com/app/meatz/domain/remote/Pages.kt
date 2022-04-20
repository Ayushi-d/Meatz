package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Pages(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("title")
        val title: String
)