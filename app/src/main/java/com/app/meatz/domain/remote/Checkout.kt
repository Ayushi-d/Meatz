package com.app.meatz.domain.remote

import com.google.gson.annotations.SerializedName

data class Checkout(
        @SerializedName("paymentUrl")
        val paymentUrl: String,
        @SerializedName("order_id")
        val order_id: Int
)