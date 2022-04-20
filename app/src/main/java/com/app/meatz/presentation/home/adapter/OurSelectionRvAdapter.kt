package com.app.meatz.presentation.home.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemHomeSelectionBinding
import com.app.meatz.domain.remote.Featured

class OurSelectionRvAdapter : BaseAdapter<ItemHomeSelectionBinding, Featured>() {
    override fun setContent(binding: ItemHomeSelectionBinding, item: Featured, position: Int) {

        item.logo.let { binding.ivShop.loadWithPlaceHolder(it) }
        item.name.let { binding.tvTitle.text = it }
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
    }
}