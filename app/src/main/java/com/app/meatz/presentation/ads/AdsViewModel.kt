package com.app.meatz.presentation.ads

import androidx.lifecycle.ViewModel
import com.app.meatz.data.preferences.loginScreenIsShow

class AdsViewModel : ViewModel() {
    fun loginScreenIsShown() = loginScreenIsShow
}