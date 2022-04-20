package com.app.meatz.domain.remote.generalResponse


import com.google.gson.annotations.SerializedName

data class Option(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        var isChecked: Boolean = false
)