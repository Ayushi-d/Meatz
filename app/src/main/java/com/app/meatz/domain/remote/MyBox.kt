package com.app.meatz.domain.remote


import com.google.gson.annotations.SerializedName

data class MyBox(
        @SerializedName("id")
        val id: Int,
        @SerializedName("items_count")
        val itemsCount: String,
        @SerializedName("is_active")
        val is_active: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("total")
        val total: String,
        var isChecked: Boolean = false
)