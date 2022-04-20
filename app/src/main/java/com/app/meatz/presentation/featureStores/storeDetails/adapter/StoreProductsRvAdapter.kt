package com.app.meatz.presentation.featureStores.storeDetails.adapter

import android.graphics.Paint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.ITEM_TYPE
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemShopProductsBinding
import com.app.meatz.domain.local.ProductsRsm
import kotlin.math.roundToInt

class StoreProductsRvAdapter : BaseAdapter<ItemShopProductsBinding, ProductsRsm>() {
    override fun setContent(binding: ItemShopProductsBinding, item: ProductsRsm, position: Int) {

        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        if (item.itemType == ITEM_TYPE) {
            binding.apply {
                PrimaryLayout.visible()
                SecondaryLayout.gone()
                ivProduct.loadWithPlaceHolder(item.productUrl, 20)
                tvProductName.text = item.productName
                tvProductPrice.text = root.context.getString(R.string.global_currency, item.productPrice)
                if (item.prdocutOldPrice.roundToInt() != 0)
                    binding.tvProductOldPrice.apply {
                        text = roundDoublePrice(item.prdocutOldPrice)
                        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
            }
        } else {
            binding.apply {
                SecondaryLayout.visible()
                PrimaryLayout.gone()
                ivBanner.loadWithPlaceHolder(item.bannerUrl)

            }
        }

    }

    fun getItemType(i: Int): String = getCurrentItems()[i].itemType

}