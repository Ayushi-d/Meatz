package com.app.meatz.domain.remote


import com.app.meatz.domain.remote.generalResponse.ProductData
import com.google.gson.annotations.SerializedName

data class Cart(
        @SerializedName("delivery")
        val delivery: String,
        @SerializedName("products")
        val products: MutableList<ProductData>,
        @SerializedName("subtotal")
        val subtotal: String,
        @SerializedName("total")
        val total: String,
        @SerializedName("can_checkout")
        val can_checkout: Int,
        @SerializedName("out_of_stock")
        val out_of_stock: List<String>
)


data class CartProductOption(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
)