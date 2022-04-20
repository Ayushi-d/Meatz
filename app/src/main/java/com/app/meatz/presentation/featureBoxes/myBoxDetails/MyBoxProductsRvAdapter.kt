package com.app.meatz.presentation.featureBoxes.myBoxDetails

import android.annotation.SuppressLint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.swipeReveal.SwipeRevealLayout
import com.app.meatz.data.utils.swipeReveal.ViewBinderHelper
import com.app.meatz.databinding.ItemMyboxProductsBinding
import com.app.meatz.domain.remote.BoxProducts

class MyBoxProductsRvAdapter : BaseAdapter<ItemMyboxProductsBinding, BoxProducts>() {
    val viewBinderHelper = ViewBinderHelper()

    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemMyboxProductsBinding, item: BoxProducts, position: Int) {

        binding.apply {

            flRemove.setOnClickListener { onViewClicked(it, item, position) }
            maincv.setOnClickListener { onViewClicked(it, item, position) }
            ivProduct.loadWithPlaceHolder(item.image, 30)
            tvProductName.text = item.name
            tvPrice.text = item.total
            tvQuantity.text = "${root.context.getString(R.string.mybox_details_quantity)} ${item.count}"
            tvStoreName.text = item.store.name
            ivShop.loadWithPlaceHolder(item.store.logo, 20)


            if (item.options.isNotEmpty()) {
                val options = item.options.joinToString { it -> it.name }
                tvOptions.text = options

            }
        }

        viewBinderHelper.bind(binding.swipRefresh, item.id.toString())
        if (LocaleHelper.locale == "en")
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_RIGHT
        else
            binding.swipRefresh.dragEdge = SwipeRevealLayout.DRAG_EDGE_LEFT
    }

}