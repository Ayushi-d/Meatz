package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Ads(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String
)

