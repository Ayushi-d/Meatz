package com.app.meatz.presentation.checkout.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.invisible
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemCheckoutDateBinding
import com.app.meatz.domain.remote.Days

class DatesRvAdapter : BaseAdapter<ItemCheckoutDateBinding, Days>() {
    private var selectedPosition = -1
    private var isOrderFromMultiVendor: Boolean = false
    override fun setContent(binding: ItemCheckoutDateBinding, item: Days, position: Int) {
        binding.apply {
            tvDayName.text = item.weekday
            tvDayNumber.text = item.day
            if (item.active == 1) {
                if (selectedPosition == position) {
                    cvDate.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.merlot))
                    tvDayName.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                    tvDayNumber.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                } else {
                    cvDate.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
                    tvDayName.setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
                    tvDayNumber.setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
                }
            } else {
                cvDate.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.grayx))
                cvDate.isEnabled = false
            }

            if (position == 0 && item.today == 1) {
                ivTodayHighlight.visible()
                if (isOrderFromMultiVendor) {
                    cvDate.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.grayx))
                    cvDate.isEnabled = false
                }
            } else
                ivTodayHighlight.invisible()

            cvDate.setOnClickListener {
                if (cvDate.isEnabled) {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onViewClicked(it, item, position)
                }
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIsOrderFromMultiVendor(_fromMultiVendor: Boolean) {
        isOrderFromMultiVendor = _fromMultiVendor
        notifyDataSetChanged()
    }
}