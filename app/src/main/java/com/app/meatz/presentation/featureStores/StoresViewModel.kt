package com.app.meatz.presentation.featureStores

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class StoresViewModel : ViewModel() {
    fun getStores(categoryId: Int) = loadData { RestClient.api.getStores(categoryId) }
    fun getStoreDetails(storeId: Int,
                        categoryId: String,
                        sort: String
    ) = loadData { RestClient.api.getStoreDetails(storeId, categoryId, sort) }

    fun getOurSelectionStores(subCategoryID: String) = loadData { RestClient.api.getFeaturesStores(subCategoryID) }
    fun getFeaturedProducts() = loadData { RestClient.api.featuredProducts() }
}