package com.app.meatz.presentation.cart.dialog

import android.content.ContentValues
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.data.application.getContext
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.presentation.featureStores.storeDetails.adapter.ChildAdapter
import kotlin.math.roundToInt

class CartChildAdapter (val list:List<ProductData>): RecyclerView.Adapter<CartChildAdapter.MyViewHolder>() {

    var onItemDeleteClick: ((Int) -> Unit)? = null
    var onItemMinusClick: ((ProductData) -> Unit)? = null
    var onItemPlusClick: ((ProductData) -> Unit)? = null

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.tvProductName) as TextView
        val tvProductPrice = itemView.findViewById(R.id.tvProductPrice) as TextView
        val ivProduct = itemView.findViewById(R.id.ivProduct) as AppCompatImageView
        val tvProductMinus = itemView.findViewById(R.id.tvProductMinus) as AppCompatImageView
        val tvProductPlus = itemView.findViewById(R.id.tvProductPlus) as AppCompatImageView
        val tvProductCount = itemView.findViewById(R.id.tvProductCount) as AppCompatTextView
        val tvProductDelete = itemView.findViewById(R.id.tvProductDelete) as AppCompatTextView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartChildAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_child_cart,parent,false)
        return CartChildAdapter.MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CartChildAdapter.MyViewHolder, position: Int) {
        Log.d(ContentValues.TAG, "onBindViewHolder child----: "+list)
        holder.productName.text = list[position].name
        holder.tvProductPrice.text = holder.itemView.context.getString(R.string.global_currency, ""+list[position].price)
        holder.tvProductCount.text = list[position].count.toString()
        holder.ivProduct.loadWithPlaceHolder(list[position].image, 20)
        if (list[position].count > 1){
            holder.tvProductMinus.setBackgroundColor(getContext().getColor(R.color.selectedRed))
        }else{
            holder.tvProductMinus.setBackgroundColor(getContext().getColor(R.color.lightCreamRed))
        }

        holder.tvProductDelete.setOnClickListener {
            onItemDeleteClick?.invoke(list[position].cart_id)
        }

        holder.tvProductMinus.setOnClickListener{
            onItemMinusClick?.invoke(list[position])
        }

        holder.tvProductPlus.setOnClickListener{
            onItemPlusClick?.invoke(list[position])
        }

    }

    override fun getItemCount(): Int {
        return  list.size
    }
}