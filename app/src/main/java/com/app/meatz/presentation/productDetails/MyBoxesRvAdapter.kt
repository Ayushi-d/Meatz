package com.app.meatz.presentation.productDetails

import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemMyBoxDialogBinding
import com.app.meatz.domain.remote.MyBox

class MyBoxesRvAdapter : BaseAdapter<ItemMyBoxDialogBinding, MyBox>() {
    override fun setContent(binding: ItemMyBoxDialogBinding, item: MyBox, position: Int) {
        binding.cbMybox.apply {
            text = item.name
            setOnClickListener {
                item.isChecked = this.isChecked
                onViewClicked(it, item, position)
            }
        }
    }
}