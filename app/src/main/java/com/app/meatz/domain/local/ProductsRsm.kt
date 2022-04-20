package com.app.meatz.domain.local

data class ProductsRsm(
        val itemType: String,
        val bannerUrl: String = "",
        val bannerType: String = "",
        val bannerItemId: Int = 0,
        val productId: Int = 0,
        val productName: String = "",
        val productUrl: String = "",
        val productPrice: String = "",
        val prdocutOldPrice: Double = 0.0,
        val productType: String
)