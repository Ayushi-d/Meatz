package com.app.meatz.presentation.featureAuth

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.loginScreenIsShow
import com.app.meatz.data.preferences.removeUser
import com.app.meatz.data.preferences.user
import com.app.meatz.domain.remote.User

class AuthViewModel : ViewModel() {
    fun setLoginScreenIsShown() {
        loginScreenIsShow = true
    }

    fun login(email: String, password: String) = loadData { RestClient.api.login(email, password) }
    fun socialLogin(hashmap: HashMap<String, Any>) = loadData { RestClient.api.socialLogin(hashmap) }
    fun cacheUserData(userData: User) {
        user = userData
    }

    fun forgetPassword(email: String) = loadData { RestClient.api.forgetPassword(email) }
    fun signup(hashmap: HashMap<String, Any>) = loadData { RestClient.api.signup(hashmap) }
    fun getPageDetails(pageId: Int) = loadData { RestClient.api.getPageDetails(pageId) }
    fun getUser() = user
    fun removerUser() = removeUser()
    fun editProfile(hashmap: HashMap<String, Any>) = loadData { RestClient.api.editProfile(hashmap) }
    fun changePassword(oldPass: String, newPass: String) = loadData { RestClient.api.changePassword(oldPass, newPass) }
}