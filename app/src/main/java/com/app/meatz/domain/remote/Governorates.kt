package com.app.meatz.domain.remote.area


import com.google.gson.annotations.SerializedName

data class Governorates(
        @SerializedName("cities")
        val areas: List<Area>,
        @SerializedName("city_id")
        val cityId: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
)

data class Area(
        @SerializedName("city_id")
        val cityId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("delivery")
        val delivery: Double,
        @SerializedName("delivery_express")
        val delivery_express: Double
)