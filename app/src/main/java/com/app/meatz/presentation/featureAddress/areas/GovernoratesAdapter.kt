package com.app.meatz.presentation.featureAddress.areas

import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.addItemDecoration
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemGovernoratesBinding
import com.app.meatz.domain.remote.area.Governorates

class GovernoratesAdapter : BaseAdapter<ItemGovernoratesBinding, Governorates>() {

    override fun setContent(binding: ItemGovernoratesBinding, item: Governorates, position: Int) {
        val areaRvAdapr = AreaAdapter()
        binding.apply {
            tvGovernorate.text = item.name
            rvAreas.apply {
                linearLayoutManager()
                adapter = areaRvAdapr
                addItemDecoration()
            }
            areaRvAdapr.fill(item.areas)
            lnGovernorate.setOnClickListener {
                if (rvAreas.isShown) {
                    ivArrow.rotation = 0F
                    rvAreas.gone()
                } else {
                    ivArrow.rotation = 180F
                    rvAreas.visible()
                }
            }

        }
        areaRvAdapr.setOnClickListener { _, _, _ ->
            onViewClicked(binding.root, item, position)
        }
    }


}







