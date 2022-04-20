package com.app.meatz.presentation.featureAddress.myAddress

import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.swipeReveal.SwipeRevealLayout
import com.app.meatz.data.utils.swipeReveal.ViewBinderHelper
import com.app.meatz.databinding.ItemAddressBinding
import com.app.meatz.domain.remote.address.Address

class AddressRvAdapter : BaseAdapter<ItemAddressBinding, Address>() {
    private val viewBinderHelper = ViewBinderHelper()
    override fun setContent(binding: ItemAddressBinding, item: Address, position: Int) {
        binding.apply {
            btnEdit.setOnClickListener { onViewClicked(it, item, position) }
            flRemove.setOnClickListener { onViewClicked(it, item, position) }
            rlMain.setOnClickListener { onViewClicked(it, item, position) }
            tvAddressName.text = item.addressName

            val addressSummary = StringBuilder()

            addressSummary.append("${item.area.name} ${root.context.getString(R.string.myaddress_block)} ${item.address.block} ${root.context.getString(R.string.myaddress_street)} ${item.address.street} ")

            addressSummary.append("${root.context.getString(R.string.myaddress_building)} ${item.address.house_number}")
            if (item.address.levelNo.isNotEmpty())
                addressSummary.append(" ${root.context.getString(R.string.myaddress_floor)} ${item.address.levelNo}")
            tvAddress.text = addressSummary
        }

        viewBinderHelper.bind(binding.swipRefresh, item.id.toString())
        if (LocaleHelper.locale == "en")
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_RIGHT
        else
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_LEFT

    }
}