package com.app.meatz.presentation.featureBoxes

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.data.preferences.isUser

class BoxesViewModel : ViewModel() {
    fun getOurBoxes() = loadData { RestClient.api.getOurBoxes() }
    fun getBoxDetails(boxId: Int) = loadData { RestClient.api.getBoxProductDetails(boxId) }
    fun addBoxToCart(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addToCart(hashMap) }
    fun deleteCart() = loadData { RestClient.api.deleteCart() }

    fun getMyBox() = loadData { RestClient.api.getMyBoxes() }
    fun deleteBox(boxId: Int) = loadData { RestClient.api.deleteBox(boxId) }
    fun addBoxToCart(boxId: Int) = loadData { RestClient.api.addBoxToMyCart(boxId) }
    fun createBox(boxName: String) = loadData { RestClient.api.createBox(boxName) }
    fun getMyBoxDetails(boxId: Int) = loadData { RestClient.api.getMyBoxDetails(boxId) }
    fun removeProductFromBox(boxId: Int, productId: String) = loadData { RestClient.api.removeProductFromBox(boxId, productId) }
    fun userIsLogged() = isUser()
}