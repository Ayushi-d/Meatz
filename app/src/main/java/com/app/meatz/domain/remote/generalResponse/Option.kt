package com.app.meatz.domain.remote.generalResponse


import com.app.meatz.domain.remote.stores.SubCategories
import com.google.gson.annotations.SerializedName

data class Option(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("product_option_items")
        val productOptionItems: List<ProductOptionItems>,
        var isChecked: Boolean = false
)

data class ProductOptionItems(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name : String,
        @SerializedName("price")
        val price: Int,
        var isChecked: Boolean = false

)