package com.app.meatz.presentation.featureStores.storeDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.databinding.DialogSortBinding


class SortDialog(context: Context) : AppCompatDialog(context) {
    private val binding = DialogSortBinding.inflate(layoutInflater)
    var getSortSelected = ""
    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            rgSort.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    when (checkedId) {
                        R.id.rbNewest -> getSortSelected = "latest"
                        R.id.rbPriceHighToLow -> getSortSelected = "high_price"
                        R.id.rbPriceLowToHigh -> getSortSelected = "low_price"
                    }


                }
            })
        }
    }

    fun performSubmit(action: () -> Unit = {}) {
        binding.btnSubmit.setOnClickListener {
            action.invoke()
        }
    }
}