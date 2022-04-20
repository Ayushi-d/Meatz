package com.app.meatz.domain.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutStatusRsm(
        val orderId: Int,
        val isCashedPayment: Boolean,
        val isPaymentSuccess: Boolean,
        val transactionId: String,
        val paymentId: String,
        val paymentAmount: String = ""
) : Parcelable