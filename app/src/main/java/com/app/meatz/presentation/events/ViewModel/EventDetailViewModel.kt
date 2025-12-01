package com.app.meatz.presentation.events.ViewModel

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class EventDetailViewModel : ViewModel() {
    fun getCarnivalDetail(sliderId: Int)   = loadData { RestClient.api.getCarnivalDetail(sliderId) }
}