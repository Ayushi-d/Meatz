package com.app.meatz.presentation.notifications

import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemNotificationBinding
import com.app.meatz.domain.remote.Notifications

class NotificationRvAdapter : BaseAdapter<ItemNotificationBinding, Notifications>() {
    override fun setContent(binding: ItemNotificationBinding, item: Notifications, position: Int) {

        binding.apply {
            root.setOnClickListener { onViewClicked(it, item, position) }
            tvNotificationMsg.text = item.text
            tvNotificationDate.text = item.createdAt
        }
    }
}