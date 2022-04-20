package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("last_name")
        val lastName: String,
        @SerializedName("mobile")
        var mobile: String? = "",
        @SerializedName("username")
        val username: String,
        @SerializedName("wallet")
        val wallet: String
)