package com.app.meatz.domain.remote


import com.app.meatz.domain.remote.box.BoxData
import com.google.gson.annotations.SerializedName

data class OfferBoxes(
        @SerializedName("boxs")
        val boxs: List<BoxData>,
        @SerializedName("categories")
        val categories: List<Category>
)