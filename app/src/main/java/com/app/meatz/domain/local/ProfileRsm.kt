package com.app.meatz.domain.local

data class ProfileRsm(
        val image: Int = 0,
        val title: String,
        val isApiPage: Boolean = false,
        val apiPageImage: String = "",
        val apiPageId: Int = 0
)