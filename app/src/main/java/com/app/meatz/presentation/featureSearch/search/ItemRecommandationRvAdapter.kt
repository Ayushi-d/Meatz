package com.app.meatz.presentation.featureSearch.search

import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemItemRecommandationBinding
import com.app.meatz.domain.remote.generalResponse.ProductData

class ItemRecommandationRvAdapter : BaseAdapter<ItemItemRecommandationBinding, ProductData>() {
    override fun setContent(binding: ItemItemRecommandationBinding, item: ProductData, position: Int) {
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        binding.apply {
            ivItem.loadWithPlaceHolder(item.image, 20)
            tvProductName.text = item.name
            tvProductPrice.text = root.context.getString(R.string.global_currency, item.price)


        }
    }

    override fun getItemCount(): Int {
        return if (getCurrentItems().size > 3)
            3
        else
            getCurrentItems().size
    }
}