package com.app.meatz.presentation.featureSearch.search

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemShopRecommandationBinding
import com.app.meatz.domain.remote.generalResponse.StoreData

class ShopRecommandationsRvAdapter : BaseAdapter<ItemShopRecommandationBinding, StoreData>() {
    override fun setContent(binding: ItemShopRecommandationBinding, item: StoreData, position: Int) {
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        binding.apply {
            ivShop.loadWithPlaceHolder(item.logo, 20)
            tvStoreName.text = item.name
        }
    }

    override fun getItemCount(): Int {
        return if (getCurrentItems().size > 3)
            3
        else
            getCurrentItems().size
    }
}