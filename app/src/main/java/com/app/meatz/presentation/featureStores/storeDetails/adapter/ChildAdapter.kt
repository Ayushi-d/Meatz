package com.app.meatz.presentation.featureStores.storeDetails.adapter

import android.content.ContentValues.TAG
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.shopDetails.CatProducts
import kotlin.math.log
import kotlin.math.roundToInt

class ChildAdapter(val list:List<ProductData>): RecyclerView.Adapter<ChildAdapter.MyViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.tvProductName) as TextView
        val tvProductPrice = itemView.findViewById(R.id.tvProductPrice) as TextView
        val tvProductOldPrice = itemView.findViewById(R.id.tvProductOldPrice) as TextView
        val ivProduct = itemView.findViewById(R.id.ivProduct) as AppCompatImageView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_products,parent,false)
        return ChildAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildAdapter.MyViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder child----: "+list)
        holder.productName.text = list[position].name
        holder.tvProductPrice.text = list[position].price
        holder.ivProduct.loadWithPlaceHolder(list[position].image, 20)
        if (list[position].priceBefore.roundToInt() != 0)
            holder.tvProductOldPrice.apply {
                text = roundDoublePrice(list[position].priceBefore)
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        holder.itemView.setOnClickListener {
           onItemClick?.invoke(list[position].id)
        }

    }

    override fun getItemCount(): Int {
        return  list.size
    }
}