package com.app.meatz.presentation.splash

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.languageSelected
import com.app.meatz.data.preferences.loginScreenIsShow
import com.app.meatz.data.preferences.notificationsAllowed

class SplashViewModel : ViewModel() {
    fun getLanguageIsSelected() = languageSelected
    fun getAds() = loadData { RestClient.api.getAds() }

    fun loginScreenIsShown() = loginScreenIsShow
    fun notificationIsAllowed() = notificationsAllowed

}