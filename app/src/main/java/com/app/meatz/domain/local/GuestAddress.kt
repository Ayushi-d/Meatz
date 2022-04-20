package com.app.meatz.domain.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuestAddress(
        val areaName: String,
        val areaId: String,
        val addressName: String,
        val addressSummary: String,
        val customerName: String,
        val customerEmail: String,
        val customerPhone: String,
        val deliveryValue: String,
        val expressDeliveryValue: String
) : Parcelable