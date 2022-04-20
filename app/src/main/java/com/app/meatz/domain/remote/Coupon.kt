package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Coupon(
        @SerializedName("copon_id")
        val coponId: Int,
        @SerializedName("discount")
        val discount: Double,
        @SerializedName("total")
        val total: Double,
        @SerializedName("used")
        val used: Int = 0
)