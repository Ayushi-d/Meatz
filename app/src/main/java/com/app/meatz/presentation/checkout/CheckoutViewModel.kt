package com.app.meatz.presentation.checkout

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class CheckoutViewModel : ViewModel() {
    fun getAreas() = loadData { RestClient.api.getAreas() }
    fun applyCoupon(code: String, total: String) = loadData { RestClient.api.applyCoupon(code, total) }
    fun getAddress() = loadData { RestClient.api.getAddress() }
    fun checkout(hashMap: HashMap<String, Any>) = loadData { RestClient.api.checkout(hashMap) }
    fun getCheckoutDetails() = loadData { RestClient.api.getCheckoutDetails() }

}