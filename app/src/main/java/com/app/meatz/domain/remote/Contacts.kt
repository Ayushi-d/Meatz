package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class Contacts(
        @SerializedName("address")
        val address: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("facebook")
        val facebook: String,
        @SerializedName("instagram")
        val instagram: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("lng")
        val lng: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("twitter")
        val twitter: String,
        @SerializedName("whatsapp")
        val whatsapp: String
)