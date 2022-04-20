package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class CheckoutDetails(
        @SerializedName("delivery")
        val delivery: Double,
        @SerializedName("discount")
        val discount: Double,
        @SerializedName("express_delivery")
        val expressDelivery: Int,
        @SerializedName("express_delivery_cost")
        val expressDeliveryCost: Double,
        @SerializedName("express_delivery_message")
        val expressDeliveryMessage: String,
        @SerializedName("subtotal")
        val subtotal: Double,
        @SerializedName("total")
        val total: Double,
        @SerializedName("wallet")
        val wallet: Double,
        @SerializedName("dates")
        val dates: Dates,
        @SerializedName("periods")
        val periods: List<Periods>
)

data class Dates(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String,
        @SerializedName("year")
        val year: String,
        @SerializedName("days")
        val days: List<Days>
)

data class Days(
        @SerializedName("weekday")
        val weekday: String,
        @SerializedName("day")
        val day: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("active")
        val active: Int,
        @SerializedName("today")
        val today: Int
)

data class Periods(
        @SerializedName("id")
        val id: Int,
        @SerializedName("from")
        val from: String,
        @SerializedName("to")
        val to: String,
        @SerializedName("active")
        val active: Int
)