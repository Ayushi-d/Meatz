package com.app.meatz.domain.remote.generalResponse

import com.google.gson.annotations.SerializedName

data class Cart(
        @SerializedName("count")
        val count: Int,
        @SerializedName("total")
        val total: Int
)