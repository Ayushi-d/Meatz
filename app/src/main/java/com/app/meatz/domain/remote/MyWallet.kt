package com.app.meatz.domain.remote.myWallet


import com.google.gson.annotations.SerializedName

data class MyWallet(
        @SerializedName("cards")
        val walletCardData: List<WalletCardData>,
        @SerializedName("wallet")
        val wallet: String
)

data class WalletCardData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("price")
        val price: String,
        @SerializedName("sort")
        val sort: Int
)