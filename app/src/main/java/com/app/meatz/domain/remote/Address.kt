package com.app.meatz.domain.remote.address


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
        @SerializedName("address")
        val address: AddressData,
        @SerializedName("address_name")
        val addressName: String,
        @SerializedName("area")
        val area: Area,
        @SerializedName("id")
        val id: Int,
        @SerializedName("full_address")
        val full_address: String = ""
) : Parcelable

@Parcelize
data class AddressData(
        @SerializedName("address_name")
        val addressName: String,
        @SerializedName("area_id")
        val areaId: String,
        @SerializedName("block")
        val block: String,
        @SerializedName("apartment_no")
        val apartment_no: String,
        @SerializedName("house_number")
        val house_number: String,
        @SerializedName("level_no")
        val levelNo: String = "",
        @SerializedName("notes")
        val notes: String,
        @SerializedName("street")
        val street: String
) : Parcelable

@Parcelize
data class Area(
        @SerializedName("city_id")
        val cityId: Int = 0,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("delivery")
        val delivery: Double,
        @SerializedName("delivery_express")
        val delivery_express: Double
) : Parcelable