package com.app.meatz.presentation.checkout.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.getStringByLocal
import com.app.meatz.databinding.ItemCheckoutTimePeriodBinding
import com.app.meatz.domain.remote.Periods
import java.util.*

class TimePeriodsRvAdapter : BaseAdapter<ItemCheckoutTimePeriodBinding, Periods>() {

    private var selectedPosition = -1
    private var todayIsClicked = false

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun setContent(binding: ItemCheckoutTimePeriodBinding, item: Periods, position: Int) {
        binding.apply {
            tvTimePeriod.text = "${item.from} ${getStringByLocal(R.string.checkout_to).toLowerCase(Locale.ROOT)} ${item.to}"

            checkFirstDayTimePeriods(binding, item, position)

            cvTime.setOnClickListener {
                if (cvTime.isEnabled) {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onViewClicked(it, item, position)
                }

            }
        }
    }

    private fun checkFirstDayTimePeriods(binding: ItemCheckoutTimePeriodBinding, item: Periods, position: Int) {
        binding.apply {
            if (item.active == 0 && todayIsClicked) {
                cvTime.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.alabaster))
                tvTimePeriod.setTextColor(ContextCompat.getColor(root.context, R.color.grayx))
                cvTime.isEnabled = false
            } else {
                if (selectedPosition == position) {
                    cvTime.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.merlot))
                    cvTime.strokeColor = ContextCompat.getColor(root.context, R.color.merlot)
                    tvTimePeriod.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                } else {
                    cvTime.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
                    cvTime.strokeColor = ContextCompat.getColor(root.context, R.color.black)
                    tvTimePeriod.setTextColor(ContextCompat.getColor(root.context, R.color.heavy_metal))
                }
                cvTime.isEnabled = true
            }
        }

    }

    fun setFirstDayIsClicked(_todayIsClicked: Boolean) {

        todayIsClicked = _todayIsClicked
        selectedPosition = -1
        notifyDataSetChanged()
    }
}