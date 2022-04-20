package com.app.meatz.presentation.featureStores.storeDetails.adapter

import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemFilterCategoryBinding
import com.app.meatz.domain.remote.Category

class FilterCategoryRvAdapter : BaseAdapter<ItemFilterCategoryBinding, Category>() {

    override fun setContent(binding: ItemFilterCategoryBinding, item: Category, position: Int) {

        binding.cbFilterCategory.text = item.name
        binding.root.setOnClickListener {
            item.isSelected = binding.cbFilterCategory.isChecked
            onViewClicked(it, item, position)
        }
    }


}