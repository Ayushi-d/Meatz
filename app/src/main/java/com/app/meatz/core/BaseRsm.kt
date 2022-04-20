package com.app.meatz.core

import com.google.gson.annotations.SerializedName

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
data class BaseRsm<T>(
        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("data")
        val data: T?,
        @SerializedName("pagination")
        val pagination: Pagination?
) {

  data class Pagination(
      @SerializedName("total")
      val total: Int,
      @SerializedName("currentPage")
      val currentPage: Int,
      @SerializedName("lastPage")
      val lastPage: Int,
      @SerializedName("perPage")
      val perPage: Int
  )
}