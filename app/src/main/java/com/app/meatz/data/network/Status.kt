package com.app.meatz.data.network

import androidx.annotation.IntDef

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 28/04/2020
 */

@IntDef(LOADING, SUCCESS, FAILURE, ERROR)
@Retention(AnnotationRetention.SOURCE)
annotation class Status

const val LOADING = 0
const val SUCCESS = 1
const val FAILURE = 2
const val ERROR = 3