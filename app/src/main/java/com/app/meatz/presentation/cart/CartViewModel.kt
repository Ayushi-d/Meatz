package com.app.meatz.presentation.cart

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.preferences.user
import com.app.meatz.domain.remote.User

class CartViewModel : ViewModel() {
    fun getCart() = loadData { RestClient.api.getCart() }
    fun isUserLogged() = isUser()
    fun getUser() = user
    fun editProfile(hashmap: HashMap<String, Any>) = loadData { RestClient.api.editProfile(hashmap) }
    fun removeFromCart(productId: Int) = loadData { RestClient.api.removeProductFromCart(productId) }
    fun updateCartItemsCount(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addToCart(hashMap) }
    fun cacheUserData(userData: User) {
        user = userData
    }

    fun clearOutOfStockItems() = loadData { RestClient.api.clearOutStockItems(1) }
}