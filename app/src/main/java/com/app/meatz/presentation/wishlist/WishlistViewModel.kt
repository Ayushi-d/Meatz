package com.app.meatz.presentation.wishlist

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class WishlistViewModel : ViewModel() {
    fun getWishlist(page: Int) = loadData { RestClient.api.getWishlist(page) }
    fun removeFromWishlist(productId: Int) = loadData { RestClient.api.likeDisLikeProduct(productId) }
}