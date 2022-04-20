package com.app.meatz.data.preferences

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.app.meatz.data.application.getContext
import com.app.meatz.domain.remote.User
import com.google.gson.Gson

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

private const val SHOW_NOTIFICATIONS = "ShowPushNotifications"
private const val LANGUAGE_SELECTED = "LanguageIsSelected"
private const val SHOW_LOGIN_SCREEN_FOR_FIRST_TIME = "LoginIsShow"
const val USER = "User"
const val IS_SUCCESS_ORDER = "IsSuceessOrder"


private val pref by lazy { getDefaultSharedPreferences(getContext()) }

var notificationsAllowed: Boolean
    get() = pref.getBoolean(SHOW_NOTIFICATIONS, true)
    set(value) = pref.edit().putBoolean(SHOW_NOTIFICATIONS, value).apply()

var languageSelected: Boolean
    get() = pref.getBoolean(LANGUAGE_SELECTED, false)
    set(value) = pref.edit().putBoolean(LANGUAGE_SELECTED, value).apply()

var loginScreenIsShow: Boolean
    get() = pref.getBoolean(SHOW_LOGIN_SCREEN_FOR_FIRST_TIME, false)
    set(value) = pref.edit().putBoolean(SHOW_LOGIN_SCREEN_FOR_FIRST_TIME, value).apply()

var user: User?
    get() = Gson().fromJson(pref.getString(USER, ""), User::class.java)
    set(value) = pref.edit().putString(USER, Gson().toJson(value)).apply()

var IsOrderSuccess: Boolean
    get() = pref.getBoolean(IS_SUCCESS_ORDER, true)
    set(value) = pref.edit().putBoolean(IS_SUCCESS_ORDER, value).apply()

fun isUser(): Boolean = user != null

fun removeUser() = remove(USER)


private fun contains(key: String): Boolean = pref.contains(key)

private fun remove(vararg keys: String) {
    for (key in keys) {
        pref.edit().remove(key).apply()
    }
}

fun clear() {
  pref.edit().clear().apply()
}

fun registerOnPrefChangeListener(listener: OnSharedPreferenceChangeListener) {
  pref.registerOnSharedPreferenceChangeListener(listener)
}

fun unregisterOnPrefChangeListener(listener: OnSharedPreferenceChangeListener) {
  pref.unregisterOnSharedPreferenceChangeListener(listener)
}