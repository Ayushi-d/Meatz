package com.app.meatz.domain.local

import com.app.meatz.R

data class SnackbarModel(
        val message: String,
        val backgroundColor: Int = R.color.merlot,
        val maxLines: Int = 1,
        val hasActions: Boolean = false,
)

