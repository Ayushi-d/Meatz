package com.app.meatz.presentation.productDetails

import android.view.View
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.databinding.MultiitemOptionBinding
import com.app.meatz.domain.remote.generalResponse.Option

class OptionsRvAdapter : BaseAdapter<MultiitemOptionBinding, Option>() {

    val multiOptionsAdapter by lazy { MultiOptionsRvAdapter() }

    override fun setContent(binding: MultiitemOptionBinding, item: Option, position: Int) {
        binding.apply {
                txtExtra.text = item.name
                 multiOptionsAdapter.produt = item.productOptionItems
                multiOptions.apply {
                    linearLayoutManager()
                    adapter = multiOptionsAdapter
                }

            multiOptionsAdapter.setonItemClickListener(object : MultiOptionsRvAdapter.onItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    onViewClicked(view,item,position)
                }

            })

        }
    }
}