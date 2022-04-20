package com.app.meatz.domain.local

data class CategoryStoreRsm(
        val itemType: String,
        val storeUrl: String = "",
        val storeName: String = "",
        val storeId: Int = 0,
        val bannerUrl: String = "",
        val bannerType: String = "",
        val bannerItemId: Int = 0,
)