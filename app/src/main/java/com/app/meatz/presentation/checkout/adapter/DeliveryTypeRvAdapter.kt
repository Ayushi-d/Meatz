package com.app.meatz.presentation.checkout.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.getStringByLocal
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemDeliveryTypeBinding
import com.app.meatz.domain.local.DeliveryTypeRsm

class DeliveryTypeRvAdapter : BaseAdapter<ItemDeliveryTypeBinding, DeliveryTypeRsm>() {

     var selectedPosition = -1
    private var isOrderFromMultiVendor: Boolean = false

    init {
        val defaultDeliveryTypeList by lazy { ArrayList<DeliveryTypeRsm>() }
        defaultDeliveryTypeList.add(DeliveryTypeRsm(getStringByLocal(R.string.checkout_Express)))
        defaultDeliveryTypeList.add(DeliveryTypeRsm(getStringByLocal(R.string.checkout_normal)))
        fill(defaultDeliveryTypeList)
    }


    override fun setContent(binding: ItemDeliveryTypeBinding, item: DeliveryTypeRsm, position: Int) {
        binding.apply {

            rbDeliveryType.text = item.deliveryType

            if (item.deliveryPrice.isEmpty()) {
                tvDeliveryPrice.gone()
                rbDeliveryType.isEnabled = false
            } else {

                tvDeliveryPrice.apply {
                    text = getStringByLocal(R.string.global_currency, item.deliveryPrice)
                    visible()
                    if (selectedPosition == position)
                        setTextColor(ContextCompat.getColor(root.context, R.color.merlot))
                    else
                        setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
                }
                rbDeliveryType.apply {
                    isEnabled = true
                    isChecked = selectedPosition == position
                }

            }
            handleViewsInCaseOfMultiVendor(binding, position, item)


            root.setOnClickListener {
                if (item.deliveryPrice.isNotEmpty() && item.isEnabled)
                    selectedPosition = position
                notifyDataSetChanged()
                onViewClicked(it, item, position)


            }
        }
    }

    @SuppressLint("ResourceType")
    private fun handleViewsInCaseOfMultiVendor(binding: ItemDeliveryTypeBinding, position: Int, item: DeliveryTypeRsm) {
        binding.apply {
            if (position == 0) {
                if (isOrderFromMultiVendor) {
                    rbDeliveryType.isEnabled = false
                    item.isEnabled = false
                    rbDeliveryType.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
                    rbDeliveryType.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.grayx))
                    rbDeliveryType.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
                    tvDeliveryPrice.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
                } else {
                    item.isEnabled = true
                    rbDeliveryType.isEnabled = true
                    rbDeliveryType.setTextColor(ContextCompat.getColorStateList(root.context, R.drawable.text_color_filter_cb))
                    rbDeliveryType.buttonDrawable = ContextCompat.getDrawable(this.root.context, R.drawable.bg_radiobtn_sort)
                    rbDeliveryType.buttonTintList = null
                    tvDeliveryPrice.setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
                }
            }


        }

    }

    fun setIsOrderFromMultiVendor(_fromMultiVendor: Boolean) {
        this.isOrderFromMultiVendor = _fromMultiVendor
        notifyDataSetChanged()
    }


}