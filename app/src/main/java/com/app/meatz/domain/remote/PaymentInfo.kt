package com.app.meatz.domain.remote.PaymentInfo


import com.google.gson.annotations.SerializedName

data class PaymentInfo(
        @SerializedName("data")
        val `data`: PaymentInfoData,
        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: String
)

data class PaymentInfoData(
        @SerializedName("order_id")
        val orderId: Int,
        @SerializedName("payment_id")
        val paymentId: String,
        @SerializedName("transaction_id")
        val transactionId: String,
        @SerializedName("amount")
        val amount: String
)