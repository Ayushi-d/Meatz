package com.app.meatz.presentation.offersBoxes.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemBoxSliderBinding

class BoxSliderVpAdapter : BaseAdapter<ItemBoxSliderBinding, String>() {
    override fun setContent(binding: ItemBoxSliderBinding, item: String, position: Int) {
        binding.apply {
            ivSlider.loadWithPlaceHolder(item)
            root.setOnClickListener { onViewClicked(it, item, position) }
        }
    }
}