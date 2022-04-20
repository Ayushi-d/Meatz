package com.app.meatz.presentation.featureAddress.areas

import androidx.lifecycle.MutableLiveData
import com.app.meatz.core.BaseAdapter
import com.app.meatz.databinding.ItemAreaBinding
import com.app.meatz.domain.remote.area.Area

class AreaAdapter : BaseAdapter<ItemAreaBinding, Area>() {

    companion object {
        val selectedArea: MutableLiveData<Area> by lazy { MutableLiveData() }
    }


    override fun setContent(binding: ItemAreaBinding, item: Area, position: Int) {
        binding.apply {
            tvArea.text = item.name
            root.setOnClickListener {
                selectedArea.value = item
                notifyDataSetChanged()
                onViewClicked(it, item, position)

            }
        }
    }

}


