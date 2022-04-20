package com.app.meatz.presentation.featureOrders

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.isUser

class OrdersViewModel : ViewModel() {
    fun getOrders() = loadData { RestClient.api.getOrders() }
    fun getOrderDetails(orderId: Int) = loadData { RestClient.api.getOrderDetails(orderId) }
    fun reOrder(orderId: Int) = loadData { RestClient.api.reorder(orderId) }
    fun cancelRequest(orderId: Int) = loadData { RestClient.api.cancelRequest(orderId) }
    fun isUserLogged() = isUser()


}