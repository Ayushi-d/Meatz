package com.app.meatz.presentation.featureOrders.orderDetails

import android.annotation.SuppressLint
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.application.ITEM_PRODUCT_TYPE
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemOrderProductsBinding
import com.app.meatz.domain.remote.orderDetails.OrderProducts

class OrderProdutsRvAdapter : BaseAdapter<ItemOrderProductsBinding, OrderProducts>() {

    @SuppressLint("SetTextI18n")
    override fun setContent(binding: ItemOrderProductsBinding, item: OrderProducts, position: Int) {

        binding.apply {
            if (item.name == ITEM_PRODUCT_TYPE) {
                clProduct.visible()
                clBox.gone()
                ivProduct.loadWithPlaceHolder(item.image, 20)
                tvProductName.text = item.title
                tvPrice.text = item.total
                tvQuantity.text = "${root.context.getString(R.string.order_details_quantity)} ${item.count}"
                item.store?.let {
                    tvStoreName.text = it.name
                    ivShop.loadWithPlaceHolder(it.logo, 20)
                }
                if (item.options.isNotEmpty()) {
                    val options = item.options.joinToString { it -> it.name }
                    tvOptions.text = options
                }


            } else {
                clProduct.gone()
                clBox.visible()
                tvBoxName.text = item.title
                tvBoxQuantity.text = "${root.context.getString(R.string.order_details_quantity)} ${item.count}"
                tvBoxPrice.text = item.total
                if (item.persons == 1)
                    tvBoxNumber.text = "1 ${root.context.getString(R.string.order_details_person)}"
                else if (item.persons > 1)
                    tvBoxNumber.text = "${item.persons} ${root.context.getString(R.string.order_details_person)}"

            }
        }

    }


}