package com.app.meatz.presentation.myWallet

import android.annotation.SuppressLint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemWalletCardsBinding
import com.app.meatz.domain.remote.myWallet.WalletCardData


class WalletCardsRvAdapter : BaseAdapter<ItemWalletCardsBinding, WalletCardData>() {
    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemWalletCardsBinding, item: WalletCardData, position: Int) {
        if (item.price.isNotEmpty() && item.price.contains(".")) {
            val priceBeforePoint = item.price.split(".")[0]
            val priceAfterPoint = item.price.split(".")[1]
            binding.apply {

                tvPriceAfterPoint.text = binding.root.context.getString(R.string.myWallet_price, priceAfterPoint)
                tvPrice.text = priceBeforePoint
                tvRecharge.setOnClickListener {
                    onViewClicked(it, item, position)
                }
            }
        }

    }
}