package com.app.meatz.presentation.featureStores.categoryStores

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.ITEM_TYPE
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemCategoryStoresBinding
import com.app.meatz.domain.local.CategoryStoreRsm

class CategoryStoresRvAdapter : BaseAdapter<ItemCategoryStoresBinding, CategoryStoreRsm>() {
    override fun setContent(binding: ItemCategoryStoresBinding, item: CategoryStoreRsm, position: Int) {

        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        if (item.itemType == ITEM_TYPE) {
            binding.apply {
                PrimaryLayout.visible()
                SecondaryLayout.gone()
                ivShop.loadWithPlaceHolder(item.storeUrl, 20)
                tvStoreName.text = item.storeName
            }
        } else {
            binding.apply {
                SecondaryLayout.visible()
                PrimaryLayout.gone()
                ivBanner.loadWithPlaceHolder(item.bannerUrl, 20)

            }
        }
    }

    fun getItemType(i: Int): String = getCurrentItems()[i].itemType
}