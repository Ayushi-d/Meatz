package com.app.meatz.presentation.cart

import android.content.ContentValues.TAG
import android.util.Log
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.ITEM_PRODUCT_TYPE
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.swipeReveal.SwipeRevealLayout
import com.app.meatz.data.utils.swipeReveal.ViewBinderHelper
import com.app.meatz.databinding.ItemCartBinding
import com.app.meatz.domain.remote.generalResponse.ProductData

class CartRvAdapter : BaseAdapter<ItemCartBinding, ProductData>() {
    private val viewBinderHelper = ViewBinderHelper()
    override fun setContent(binding: ItemCartBinding, item: ProductData, position: Int) {
        binding.apply {
            flRemoveProduct.setOnClickListener { onViewClicked(it, item, position) }
            flRemoveBox.setOnClickListener { onViewClicked(it, item, position) }
            tvBoxPlus.setOnClickListener { onViewClicked(it, item, position) }
            tvProductPlus.setOnClickListener { onViewClicked(it, item, position) }
            tvBoxMinus.setOnClickListener { onViewClicked(it, item, position) }
            tvProductMinus.setOnClickListener { onViewClicked(it, item, position) }
            if (item.type == ITEM_PRODUCT_TYPE)
                handleProductsView(binding, item,position)
            else
                handleBoxViews(binding, item)
        }
    }

    private fun handleProductsView(binding: ItemCartBinding, item: ProductData,position: Int) {
        binding.apply {
            rootProduct.visible()
            rootBox.gone()
            ivProduct.loadWithPlaceHolder(item.image, 20)
            tvProductName.text = item.name
            //Log.d(TAG, "handleProductsView: "+item.price+"double---"+item.price.toDouble()+"option---"+item.options.get(position).price)
            tvProductPrice.text = root.context.getString(R.string.global_currency, ""+item.price)
            if (item.options.isNotEmpty()) {
                val options = item.options.joinToString { it -> it.name }
                tvOptions.text = options

            }
            if (item.store != null) {
                tvStoreName.text = item.store.name
                ivShop.loadWithPlaceHolder(item.store.logo, 20)
            }
            viewBinderHelper.bind(rootProduct, item.id.toString())
            if (LocaleHelper.locale == "en")
                rootProduct.dragEdge = SwipeRevealLayout.DRAG_EDGE_RIGHT
            else
                rootProduct.dragEdge = SwipeRevealLayout.DRAG_EDGE_LEFT


            tvProductCount.text = item.count.toString()
        }
    }

    private fun handleBoxViews(binding: ItemCartBinding, item: ProductData) {
        binding.apply {
            rootBox.visible()
            rootProduct.gone()
            tvBoxName.text = item.name
            tvBoxPrice.text = root.context.getString(R.string.global_currency, item.price)
            tvBoxCount.text = item.count.toString()
            tvBoxPerson.text = root.context.getString(R.string.home_persons, item.persons.toString())
            viewBinderHelper.bind(rootBox, item.id.toString())
            if (LocaleHelper.locale == "en")
                rootBox.dragEdge = SwipeRevealLayout.DRAG_EDGE_RIGHT
            else
                rootBox.dragEdge = SwipeRevealLayout.DRAG_EDGE_LEFT
        }
    }


}