package com.app.meatz.presentation.settings

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class SettingsViewModel : ViewModel() {
    fun getContacts() = loadData { RestClient.api.getContacts() }
    fun sendMessage(hashmap: HashMap<String, String>) = loadData { RestClient.api.sendMessage(hashMap = hashmap) }
    fun getPage(pageId: Int) = loadData { RestClient.api.getPageDetails(pageId) }
    fun getPages() = loadData { RestClient.api.getPages() }
}