package com.app.meatz.presentation.featureOrders.myOrders

import android.annotation.SuppressLint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemOrdersBinding
import com.app.meatz.domain.remote.Orders

class MyOrdersRvAdapter : BaseAdapter<ItemOrdersBinding, Orders>() {
    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemOrdersBinding, item: Orders, position: Int) {
        binding.apply {
            root.setOnClickListener { onViewClicked(it, item, position) }
            tvReorder.setOnClickListener { onViewClicked(it, item, position) }
            tvOrderId.text = "#${item.id}"
            tvOrderDate.text = item.createdAt
            tvPrice.text = root.context.getString(R.string.global_currency, item.total)
            tvStatus.text = item.status
            tvItemsNumber.text = "${root.context.getString(R.string.myorders_items)} ${item.items_count}"
            if (item.canReorder == 1)
                binding.tvReorder.visible()
        }
    }

}