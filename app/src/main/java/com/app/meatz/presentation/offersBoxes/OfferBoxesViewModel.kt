package com.app.meatz.presentation.offersBoxes

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class OfferBoxesViewModel : ViewModel() {
    fun getOfferBoxes(keyword: String, categoryId: String) = loadData { RestClient.api.getOfferBoxes(keyword, categoryId) }
    fun getOfferBoxDetails(boxId: Int) = loadData { RestClient.api.getOfferBoxDetails(boxId) }
    fun addBoxToCart(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addToCart(hashMap) }
}