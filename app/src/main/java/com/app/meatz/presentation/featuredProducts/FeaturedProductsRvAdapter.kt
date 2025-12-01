package com.app.meatz.presentation.featuredProducts

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
import com.app.meatz.domain.remote.FeaturedProducts
import kotlin.math.roundToInt

class FeaturedProductsRvAdapter : BaseAdapter<ItemShopProductsBinding, FeaturedProducts>() {

    override fun setContent(
        binding: ItemShopProductsBinding,
        item: FeaturedProducts,
        position: Int
    ) {

        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        binding.apply {
            PrimaryLayout.visible()
            SecondaryLayout.gone()
            ivProduct.loadWithPlaceHolder(item.image, 20)
            tvProductName.text = item.name
            tvProductPrice.text =
                root.context.getString(R.string.global_currency, item.price)
            if (item.price.toDouble().roundToInt() != 0)
                binding.tvProductOldPrice.apply {
                    text = roundDoublePrice(item.price.toDouble())
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
        }

    }
}
