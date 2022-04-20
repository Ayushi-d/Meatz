package com.app.meatz.domain.remote


import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class Search(
        @SerializedName("products")
        val products: List<ProductData>,
        @SerializedName("stores")
        val stores: List<StoreData>
)