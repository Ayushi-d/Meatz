package com.app.meatz.presentation.featureSearch.searchResults

import android.graphics.Paint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.databinding.ItemProductSearchBinding
import com.app.meatz.domain.remote.generalResponse.ProductData
import kotlin.math.roundToInt

class ProductSearchResultsRvAdapter : BaseAdapter<ItemProductSearchBinding, ProductData>() {
    override fun setContent(binding: ItemProductSearchBinding, item: ProductData, position: Int) {

        binding.apply {
            ivProduct.loadWithPlaceHolder(item.image, 20)
            tvProductName.text = item.name
            tvProductPrice.text = root.context.getString(R.string.global_currency, item.price)
            if (item.priceBefore.roundToInt() != 0)
                binding.tvProductOldPrice.apply {
                    text = roundDoublePrice(item.priceBefore)
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

            root.setOnClickListener { onViewClicked(it, item, position) }
        }
    }

}