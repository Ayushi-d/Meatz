package com.app.meatz.data.network

import android.content.Intent
import android.util.Log
import androidx.lifecycle.liveData
import com.app.meatz.R
import com.app.meatz.core.BaseRsm
import com.app.meatz.data.application.getContext
import com.app.meatz.data.application.getStringByLocal
import com.app.meatz.data.network.Resource.Companion.error
import com.app.meatz.data.network.Resource.Companion.failure
import com.app.meatz.data.network.Resource.Companion.success
import com.app.meatz.data.preferences.removeUser
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import retrofit2.Response
import java.io.IOException

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

fun <T> loadData(request: suspend () -> Response<BaseRsm<T>>) = liveData(Dispatchers.IO) {
    emit(Resource.loading())
    try {
        val response = request.invoke()
        val rsm = if (response.isSuccessful) response.body() else {
            val type = object : TypeToken<BaseRsm<T>>() {}.type
            Gson().fromJson(response.errorBody()!!.charStream(), type) as BaseRsm<T>
        }

        if (response.isSuccessful && rsm != null) {
            if (response.code() == 200) {
                Log.i("responseStatus", rsm.status)
                if (rsm.status == "success")
                    emit(success(rsm.data, rsm.message, rsm.pagination))
                else if (rsm.status == "error")
                    emit(error(rsm.data, rsm.message))
            }

        } else {
            if (response.code() == 401) {
                val intent = Intent(getContext(), AuthActivity::class.java)
                intent.clearTask().newTask()
                getContext().startActivity(intent)
                removeUser()
            } else {
                Log.i("e", "emitFailure")
                emit(failure(rsm?.message ?: getStringByLocal(
                        R.string.network_service_error, response.code(), response.message())))
            }
        }
    } catch (e: Exception) {
        Log.e("okhttp", "$e")
        emit(failure(getErrorMsg(e)))
    }
}

/*fun loadGeoData(request: suspend () -> Response<GeoCodingRsm>) = liveData(Dispatchers.IO) {
  emit(Resource.loading())
  try {
    val response = request.invoke()
    val rsm = response.body()
    if (response.isSuccessful && rsm != null) {
      when (rsm.status) {
        "OK" -> emit(success(rsm, ""))
        else -> emit(failure(rsm.errorMessage))
      }
    } else emit(failure(rsm?.errorMessage
        ?: getStringByLocal(R.string.network_service_error,
            response.code(), response.message())))
  } catch (e: Exception) {
    emit(failure(getErrorMsg(e)))
  }
}*/

fun getErrorMsg(e: Exception) = when (e) {
    is IOException -> // Timeout
        getStringByLocal(R.string.network_check_connection)
    is RuntimeException -> // Unexpected Json response from server
        getStringByLocal(R.string.network_unexpected_response)
    else -> // Other error
        getStringByLocal(R.string.network_server_unreachable)
}