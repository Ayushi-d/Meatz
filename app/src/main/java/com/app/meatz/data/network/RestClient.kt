@file:Suppress("DEPRECATION")

package com.app.meatz.data.network

import android.util.Log
import com.app.meatz.BuildConfig.DEBUG
import com.app.meatz.data.preferences.user
import com.app.meatz.data.utils.LocaleHelper.locale
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
object RestClient {

  //TestUrl=https://linekw.org/meatz/api/
  //LiveUrl=https://meatz-app.com/api/
 // private const val BASE_URL = "http://meatz.testingjunction.tech/api/"
  private const val BASE_URL = "http://meatz-app.com/api/"
  //private const val BASE_URL = "https://meatz.test/api/"

  val api: ApiService by lazy {
    Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build().create(ApiService::class.java)
  }

  private val httpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor(headerInterceptor())
        .addInterceptor(loggingInterceptor())
//        .authenticator(TokenAuthenticator())
        .build()
  }

  private fun headerInterceptor(): Interceptor {
    return Interceptor {
      val request = it.request().newBuilder()
              .header("Accept", "application/json")
              .header("Platform", "android")
              .header("lang", locale)
              .header("FbToken", FirebaseInstanceId.getInstance().token ?: "")
              .header("Authorization", "Bearer ${user?.accessToken}")

          .build()

      Log.i("language", locale)
      it.proceed(request)
    }
  }

  private fun loggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      level = if (DEBUG) BODY else NONE
    }
  }
}

private class TokenAuthenticator : Authenticator {

  override fun authenticate(route: Route?, response: Response): Request {
    val updatedToken = getUpdatedToken()
    return response.request.newBuilder()
        .header("Content-Type", "application/json")
        .header("Platform", "android")
        .header("FbToken", FirebaseInstanceId.getInstance().token ?: "")
        .header("Lang", locale)
        .header("Authorization", updatedToken)
        .build()
  }

  private fun getUpdatedToken(): String {
    /*val authTokenResponse = liveData(Dispatchers.IO) { emit(RestClient.api.getAds().body()) }
    val newToken = authTokenResponse.value!!.message*/
    // TODO: 2/19/21 Save new token in shared pref.
    return /*newToken*/ ""
  }
}