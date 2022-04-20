package com.app.meatz.presentation.languageSelection

import androidx.lifecycle.ViewModel
import com.app.meatz.data.preferences.languageSelected
import com.app.meatz.data.preferences.loginScreenIsShow
import com.app.meatz.data.preferences.notificationsAllowed
import com.app.meatz.data.utils.LocaleHelper

class LanguageSelectionViewModel : ViewModel() {

    fun setLanguageIsSelected() {
        languageSelected = true
    }

    fun changeLocale(lang: String) {
        LocaleHelper.locale = lang
    }

    fun notificationIsAllowed() = notificationsAllowed
    fun loginScreenIsShown() = loginScreenIsShow

}