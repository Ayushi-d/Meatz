package com.app.meatz.domain.remote

import com.google.gson.annotations.SerializedName

data class LikeDislike(
        @SerializedName("liked")
        val liked: Int
)