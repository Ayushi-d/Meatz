package com.app.meatz.domain.local

import com.app.meatz.domain.remote.generalResponse.Tags

data class CategoryStoreRsm(
        val itemType: String,
        val storeUrl: String = "",
        val storeName: String = "",
        val storeId: Int = 0,
        val bannerUrl: String = "",
        val bannerType: String = "",
        val bannerItemId: Int = 0,
        val rating: String = "0.0",
        val tags: List<Tags>? = null
)