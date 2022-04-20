package com.app.meatz.presentation.notifications

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class NotificationsViewModel : ViewModel() {

    fun getNotifications(page: Int) = loadData { RestClient.api.getNotifications(page) }
}