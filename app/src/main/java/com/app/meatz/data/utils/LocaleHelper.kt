package com.app.meatz.data.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import java.util.*

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
@SuppressLint("StaticFieldLeak")
object LocaleHelper {

  private const val SELECTED_LANGUAGE = "AppliedLanguage"
  private val pref by lazy { getDefaultSharedPreferences(context) }
  private lateinit var context: Context

  var locale: String
    get() = pref.getString(SELECTED_LANGUAGE, Locale.getDefault().language) as String
    set(language) {
      pref.edit().putString(SELECTED_LANGUAGE, language).apply()
      updateResources(locale)
    }

  fun onAttach(context: Context): Context {
    this.context = context
    return updateResources(locale)
  }

  private fun updateResources(language: String): Context {
    val locale = Locale(language, if (language == "ar") "EG" else "US")
    Locale.setDefault(locale)

    val configuration = context.resources.configuration
    configuration.setLocale(locale)
    configuration.setLayoutDirection(locale)

    return context.createConfigurationContext(configuration)
  }
}