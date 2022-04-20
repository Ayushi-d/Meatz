package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class DeleteCart(
        @SerializedName("cart")
        val cart: Int
)