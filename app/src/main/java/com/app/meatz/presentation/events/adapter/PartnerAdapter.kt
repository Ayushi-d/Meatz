package com.app.meatz.presentation.events.adapter

import android.view.View
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemHomeSelectionBinding
import com.app.meatz.domain.remote.Featured
import com.app.meatz.domain.remote.Partners

class PartnerAdapter:  BaseAdapter<ItemHomeSelectionBinding, Partners>(){
    override fun setContent(binding: ItemHomeSelectionBinding, item: Partners, position: Int) {
        binding.tvTitle.visibility = View.GONE
        binding.ivShop.loadWithPlaceHolder(item.logo)
    }
    }