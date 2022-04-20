package com.app.meatz.presentation.productDetails

import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemOptionBinding
import com.app.meatz.domain.remote.generalResponse.Option

class OptionsRvAdapter : BaseAdapter<ItemOptionBinding, Option>() {
    override fun setContent(binding: ItemOptionBinding, item: Option, position: Int) {
        binding.apply {
            tvOptionPrice.text = root.context.getString(R.string.global_currency, item.price)
            cbOption.text = item.name


            cbOption.setOnClickListener {
                onViewClicked(it, item, position)
            }

        }

    }
}