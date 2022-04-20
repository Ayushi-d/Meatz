package com.app.meatz.presentation.featureSearch

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class SearchViewModel : ViewModel() {

    fun search(keyword: String, isMore: Int) = loadData { RestClient.api.search(keyword, isMore) }
}