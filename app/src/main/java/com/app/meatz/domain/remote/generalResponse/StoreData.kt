package com.app.meatz.domain.remote.generalResponse

import com.google.gson.annotations.SerializedName

data class StoreData(
        @SerializedName("color")
        val color: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("logo")
        val logo: String,
        @SerializedName("name")
        val name: String
)