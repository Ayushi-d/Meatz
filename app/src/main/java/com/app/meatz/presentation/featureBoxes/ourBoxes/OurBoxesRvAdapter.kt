package com.app.meatz.presentation.featureBoxes.ourBoxes

import android.annotation.SuppressLint
import android.graphics.Paint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.databinding.ItemOurBoxesBinding
import com.app.meatz.domain.remote.box.BoxData
import kotlin.math.roundToInt

class OurBoxesRvAdapter : BaseAdapter<ItemOurBoxesBinding, BoxData>() {
    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemOurBoxesBinding, item: BoxData, position: Int) {
        binding.root.setOnClickListener { onViewClicked(it, item, position) }
        item.image.let { binding.ivBox.loadWithPlaceHolder(it, 20) }
        item.name.let { binding.tvBoxName.text = it }
        binding.tvNumberPerson.text = binding.root.context.getString(R.string.home_persons, item.persons.toString())
        binding.tvBoxPrice.text = binding.root.context.getString(R.string.global_currency, item.price)
        if (item.priceBefore.roundToInt() != 0) {
            binding.tvBoxOldPrice.apply {
                text = roundDoublePrice(item.priceBefore)
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

        }
    }
}