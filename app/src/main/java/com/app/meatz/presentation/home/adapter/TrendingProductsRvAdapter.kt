package com.app.meatz.presentation.home.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemHomeSelectionBinding
import com.app.meatz.domain.remote.Featured
import com.app.meatz.domain.remote.FeaturedProducts

class TrendingProductsRvAdapter : BaseAdapter<ItemHomeSelectionBinding, FeaturedProducts>() {
    override fun setContent(binding: ItemHomeSelectionBinding, item: FeaturedProducts, position: Int) {
        item.image.let { binding.ivShop.loadWithPlaceHolder(it) }
        item.name.let { binding.tvTitle.text = it }
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
    }
}