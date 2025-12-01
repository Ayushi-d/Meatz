package com.app.meatz.presentation.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.loadWithPlaceHolderRound
import com.app.meatz.domain.remote.StoreProducts
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.shopDetails.CatProducts
import com.app.meatz.presentation.cart.dialog.CartChildAdapter
import com.app.meatz.presentation.featureStores.storeDetails.adapter.ChildAdapter
import com.app.meatz.presentation.featureStores.storeDetails.adapter.ParentAdapter

class CartParentAdapter(val list:List<StoreProducts>): RecyclerView.Adapter<CartParentAdapter.MyViewHolder>() {

    var onItemDeleteClick: ((Int) -> Unit)? = null
    var onItemMinusClick: ((ProductData) -> Unit)? = null
    var onItemPlusClick: ((ProductData) -> Unit)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText = itemView.findViewById(R.id.header_title) as TextView
        val storeLogo = itemView.findViewById(R.id.storeLogo) as AppCompatImageView
        val recylerList = itemView.findViewById(R.id.rvCartProducts) as RecyclerView
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartParentAdapter.MyViewHolder {
        val header = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_header_cart, parent, false)
        return CartParentAdapter.MyViewHolder(header)
    }

    override fun onBindViewHolder(holder: CartParentAdapter.MyViewHolder, position: Int) {
        holder.headerText.text = list[position].storeName
        holder.storeLogo.loadWithPlaceHolderRound(list[position].storeLogo)
        val childAdapter = CartChildAdapter(list[position].products)
        holder.recylerList.apply {
            adapter = childAdapter
        }

        childAdapter.onItemDeleteClick = {
            onItemDeleteClick?.invoke(it)
        }
        childAdapter.onItemMinusClick = {
            onItemMinusClick?.invoke(it)
        }
        childAdapter.onItemPlusClick = {
            onItemPlusClick?.invoke(it)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}