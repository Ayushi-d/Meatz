package com.app.meatz.presentation.productDetails

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class ProductDetailsViewModel : ViewModel() {

    fun getProductDetails(productId: Int) = loadData { RestClient.api.getProductDetails(productId) }
    fun favUnFav(productId: Int) = loadData { RestClient.api.likeDisLikeProduct(productId) }
    fun addProductToCart(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addToCart(hashMap) }
    fun deleteCart() = loadData { RestClient.api.deleteCart() }

    fun getMyBox() = loadData { RestClient.api.getMyBoxes() }
    fun addItemToBoxes(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addItemToBoxes(hashMap) }
    fun deleteBoxItems(boxId: Int) = loadData { RestClient.api.deleteBoxItems(boxId) }
}