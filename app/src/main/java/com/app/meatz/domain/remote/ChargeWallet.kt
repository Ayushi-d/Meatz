package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class ChargeWallet(
        @SerializedName("paymentUrl")
        val paymentUrl: String
)