package com.app.meatz.presentation.events.ViewModel

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class AddEventViewModel: ViewModel() {

    fun getCarnivalTicket() = loadData { RestClient.api.getCarnivalTickets() }
    fun getCarnivalKhashtaSize() = loadData { RestClient.api.getCarnivalKhashtaSizes() }
    fun carnivalBooking(hashmap: HashMap<String, Any>) = loadData { RestClient.api.carnivalBooking(hashmap) }
}