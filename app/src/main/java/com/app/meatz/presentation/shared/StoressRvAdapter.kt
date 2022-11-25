package com.app.meatz.presentation.shared

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemShopBinding
import com.app.meatz.domain.remote.generalResponse.StoreData

class StoressRvAdapter : BaseAdapter<ItemShopBinding, StoreData>() {

    override fun setContent(binding: ItemShopBinding, item: StoreData, position: Int) {
        item.banner.let { binding.ivShop.loadWithPlaceHolder(it) }
        item.logo.let { binding.ivStoreLogo.loadWithPlaceHolder(it) }
        binding.tvTitle.text = item.name
        binding.ivStoreRating.text = if (item.rating.isNullOrBlank())   "0.0 " else item.rating + " "
        if (item.tags!!.isNotEmpty())
            binding.tvStoreTags.text = item.tags!!.joinToString { it.name }
        else
            binding.tvStoreTags.text = "----"
        binding.root.setOnClickListener { onViewClicked(it, item, position) }

    }
}