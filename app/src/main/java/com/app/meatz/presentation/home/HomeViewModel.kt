package com.app.meatz.presentation.home

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.isUser

class HomeViewModel : ViewModel() {
    fun getHome() = loadData { RestClient.api.getHome() }
    fun isUserLogged() = isUser()
}