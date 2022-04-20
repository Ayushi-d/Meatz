package com.app.meatz.presentation.checkout.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.getStringByLocal
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.roundPrice
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemPaymentMethodBinding
import kotlin.math.roundToInt


class PaymentMethodRvAdapter : BaseAdapter<ItemPaymentMethodBinding, String>() {

    init {

        val paymentMethods by lazy { ArrayList<String>() }
        paymentMethods.add(getStringByLocal(R.string.checkout_online))
        paymentMethods.add(getStringByLocal(R.string.checkout_wallet))
        fill(paymentMethods)
    }

    var totalCheckout = 0.000
    private var walletValue = 0.000
    var selectedPosition = 0
    override fun setContent(binding: ItemPaymentMethodBinding, item: String, position: Int) {
        binding.apply {
            if (position == 0)
                tvWalletValue.gone()
            else
                setWalletView(binding)


            rbPaymentMethod.text = item
            rbPaymentMethod.isChecked = selectedPosition == position
            rbPaymentMethod.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onViewClicked(it, item, position)
            }
        }
    }

    //regionWallet
    private fun setWalletView(binding: ItemPaymentMethodBinding) {
        binding.apply {
            tvWalletValue.visible()
            tvWalletValue.text = getStringByLocal(R.string.global_currency, roundPrice(walletValue.roundToInt()))
            if (walletValue.roundToInt() == 0 || walletValue.roundToInt() < totalCheckout.roundToInt())
                disableWalletView(binding)
            else
                enableWalletView(binding)
        }
    }

    private fun disableWalletView(binding: ItemPaymentMethodBinding) {
        if (selectedPosition != 0)
            selectedPosition = -1

        binding.apply {
            rbPaymentMethod.isEnabled = false
            rbPaymentMethod.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
            rbPaymentMethod.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.grayx))
            tvWalletValue.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
        }
    }

    @SuppressLint("ResourceType")
    private fun enableWalletView(binding: ItemPaymentMethodBinding) {
        binding.apply {
            rbPaymentMethod.isEnabled = true
            rbPaymentMethod.setTextColor(ContextCompat.getColorStateList(root.context, R.drawable.text_color_filter_cb))
            rbPaymentMethod.buttonDrawable = ContextCompat.getDrawable(this.root.context, R.drawable.bg_radiobtn_sort)
            rbPaymentMethod.buttonTintList = null
            tvWalletValue.setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
        }
    }

    fun setWalletValue(_wallet: Double) {
        walletValue = _wallet
        notifyDataSetChanged()

    }

    //endregion
    fun setTotalValue(_total: Double) {
        this.totalCheckout = _total
        notifyDataSetChanged()
    }


}