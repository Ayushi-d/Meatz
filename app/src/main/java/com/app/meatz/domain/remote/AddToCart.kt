package com.app.meatz.domain.remote.addCart


import com.google.gson.annotations.SerializedName

data class AddToCart(
        @SerializedName("delivery")
        val delivery: String,
        @SerializedName("products")
        val products: List<Product>,
        @SerializedName("store")
        val store: Store,
        @SerializedName("subtotal")
        val subtotal: String,
        @SerializedName("total")
        val total: String
)

data class Product(
        @SerializedName("count")
        val count: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("options_txt")
        val optionsTxt: String,
        @SerializedName("price")
        val price: String
)

data class Store(
        @SerializedName("areas")
        val areas: List<Int>,
        @SerializedName("days_off")
        val daysOff: List<String>,
        @SerializedName("id")
        val id: Int,
        @SerializedName("mode")
        val mode: String,
        @SerializedName("working_days")
        val workingDays: List<Int>,
        @SerializedName("working_from")
        val workingFrom: String,
        @SerializedName("working_to")
        val workingTo: String
)

data class Option(
        @SerializedName("count")
        val count: Int,
        @SerializedName("id")
        val id: Int
)