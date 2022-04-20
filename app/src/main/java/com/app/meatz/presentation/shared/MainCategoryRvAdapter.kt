package com.app.meatz.presentation.shared

import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.databinding.ItemMainCategoryBinding
import com.app.meatz.domain.remote.Category

class MainCategoryRvAdapter(var selectedOption: Boolean = true) : BaseAdapter<ItemMainCategoryBinding, Category>() {
    private var selectedposition: Int = -1
    override fun setContent(binding: ItemMainCategoryBinding, item: Category, position: Int) {

        binding.tvCategory.text = item.name
        binding.ivCategory.loadWithPlaceHolder(item.image)

        if (selectedOption) {
            if (selectedposition == position) {
                binding.flCategory.apply {
                    strokeColor = ContextCompat.getColor(binding.root.context, R.color.merlot)
                    strokeWidth = 7
                }
            } else {
                binding.flCategory.apply {
                    setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.transparent))
                    strokeWidth = 0
                }
            }
        }
        binding.root.setOnClickListener {
            if (selectedOption) {
                selectedposition = position
                notifyItemChanged(position)
                notifyDataSetChanged()
            }
            onViewClicked(it, item, position)
        }

    }

}