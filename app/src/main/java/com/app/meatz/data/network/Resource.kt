package com.app.meatz.data.network

import com.app.meatz.core.BaseRsm

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
data class Resource<out T>(
    @Status val status: Int,
    val message: String = "",
    val data: T? = null,
    val pagination: BaseRsm.Pagination? = null
) {

  companion object {

      fun loading(): Resource<Nothing> = Resource(LOADING)

      fun <T> success(data: T?, message: String, pagination: BaseRsm.Pagination? = null): Resource<T> =
              Resource(status = SUCCESS, data = data, message = message, pagination = pagination)

      fun <T> error(data: T?, message: String): Resource<T> = Resource(status = ERROR, message = message, data = data)

      fun failure(message: String): Resource<Nothing> = Resource(status = FAILURE, message = message)
  }
}