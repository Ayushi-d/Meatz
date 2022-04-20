package com.app.meatz.presentation.home.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemBannerBinding
import com.app.meatz.domain.remote.Slider

class BannerVpAdapter : BaseAdapter<ItemBannerBinding, Slider>() {
    override fun setContent(binding: ItemBannerBinding, item: Slider, position: Int) {

        item.image?.let { binding.ivBanner.loadWithPlaceHolder(it, 20) }
        binding.root.setOnClickListener { onViewClicked(it, item, position) }

    }
}