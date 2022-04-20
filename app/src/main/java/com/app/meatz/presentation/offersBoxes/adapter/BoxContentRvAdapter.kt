package com.app.meatz.presentation.offersBoxes.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemBoxDescriptionBinding

class BoxContentRvAdapter : BaseAdapter<ItemBoxDescriptionBinding, String>() {
    override fun setContent(binding: ItemBoxDescriptionBinding, item: String, position: Int) {
        binding.tvTitle.text = item
    }
}