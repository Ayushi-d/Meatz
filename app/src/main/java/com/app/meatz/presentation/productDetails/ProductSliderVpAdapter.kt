package com.app.meatz.presentation.productDetails

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemBoxSliderBinding
import com.app.meatz.domain.remote.productDetails.Image


class ProductSliderVpAdapter : BaseAdapter<ItemBoxSliderBinding, Image>() {
    override fun setContent(binding: ItemBoxSliderBinding, item: Image, position: Int) {
        binding.apply {
            ivSlider.loadWithPlaceHolder(item.image)
            root.setOnClickListener { onViewClicked(it, item, position) }
        }
    }
}