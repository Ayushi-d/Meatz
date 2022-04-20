package com.app.meatz.domain.remote

import com.google.gson.annotations.SerializedName

data class ForgetPassword(
        @SerializedName("code")
        val code: String,
        @SerializedName("email")
        val email: String
)