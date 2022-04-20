package com.app.meatz.presentation.shared

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemShopBinding
import com.app.meatz.domain.remote.generalResponse.StoreData

class StoressRvAdapter : BaseAdapter<ItemShopBinding, StoreData>() {
    override fun setContent(binding: ItemShopBinding, item: StoreData, position: Int) {
        item.logo.let { binding.ivShop.loadWithPlaceHolder(it) }
        binding.tvTitle.text = item.name
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
    }
}