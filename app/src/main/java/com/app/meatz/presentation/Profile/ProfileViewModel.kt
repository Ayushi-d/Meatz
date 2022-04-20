package com.app.meatz.presentation.Profile

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.preferences.removeUser

class ProfileViewModel : ViewModel() {

    fun removerUser() = removeUser()
    fun isUserLogged() = isUser()
    fun getProfileInfo() = loadData { RestClient.api.getUserProfile() }
    fun logout() = loadData { RestClient.api.logout() }
}