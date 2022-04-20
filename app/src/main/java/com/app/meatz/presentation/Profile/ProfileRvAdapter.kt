package com.app.meatz.presentation.Profile

import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.load
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemProfileBinding
import com.app.meatz.domain.local.ProfileRsm

class ProfileRvAdapter : BaseAdapter<ItemProfileBinding, ProfileRsm>() {
    private var walletValue: String = ""
    override fun setContent(binding: ItemProfileBinding, item: ProfileRsm, position: Int) {
        binding.apply {
            swNotifiction.gone()
            if (position == 0) {
                tvWalletValue.apply {
                    visible()
                    if (walletValue.isNotEmpty())
                        text = binding.root.context.getString(R.string.global_currency, walletValue)
                }
            }
            root.setOnClickListener { onViewClicked(it, item, position) }
            ivProfile.load(item.image)
            tvTitle.text = item.title
        }
    }

    fun setWalletValue(_wallet: String) {
        walletValue = _wallet
        notifyDataSetChanged()
    }
}