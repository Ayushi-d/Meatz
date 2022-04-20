package com.app.meatz.domain.local

data class DeliveryTypeRsm(
        val deliveryType: String,
        var deliveryPrice: String = "",
        var isEnabled: Boolean = true
)