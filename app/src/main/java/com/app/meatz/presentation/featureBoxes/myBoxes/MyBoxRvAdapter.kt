package com.app.meatz.presentation.featureBoxes.myBoxes

import android.annotation.SuppressLint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.swipeReveal.SwipeRevealLayout
import com.app.meatz.data.utils.swipeReveal.ViewBinderHelper
import com.app.meatz.databinding.ItemMyBoxBinding
import com.app.meatz.domain.remote.MyBox

class MyBoxRvAdapter : BaseAdapter<ItemMyBoxBinding, MyBox>() {
    val viewBinderHelper = ViewBinderHelper()

    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemMyBoxBinding, item: MyBox, position: Int) {
        binding.apply {
            btnView.setOnClickListener { onViewClicked(it, item, position) }
            btnAddCart.setOnClickListener { onViewClicked(it, item, position) }
            flRemove.setOnClickListener { onViewClicked(it, item, position) }
            rlMain.setOnClickListener { onViewClicked(it, item, position) }
            if (item.itemsCount == "0") {
                btnAddCart.gone()
                tvEmptyItems.visible()
            }

            tvBoxName.text = item.name
            tvBoxPrice.text = root.context.getString(R.string.global_currency, item.total)
            tvItemsNumber.text = "${root.context.getString(R.string.mybox_items)} ${item.itemsCount}"

        }
        viewBinderHelper.bind(binding.swipRefresh, item.id.toString())
        if (LocaleHelper.locale == "en")
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_RIGHT
        else
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_LEFT
    }
}