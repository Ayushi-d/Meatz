package com.app.meatz.presentation.featureStores.storeDetails.adapter

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.shopDetails.CatProducts
import org.jetbrains.anko.sdk27.coroutines.onClick

class ParentAdapter(val list:List<CatProducts>): RecyclerView.Adapter<ParentAdapter.MyViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         val headerText = itemView.findViewById(R.id.header_title) as TextView
         val recylerList = itemView.findViewById(R.id.rvListProducts) as RecyclerView
     }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentAdapter.MyViewHolder {
        val header = LayoutInflater.from(parent.context).inflate(R.layout.item_header_products,parent,false)
        return ParentAdapter.MyViewHolder(header)
    }

    override fun onBindViewHolder(holder: ParentAdapter.MyViewHolder, position: Int) {
        holder.headerText.text = list[position].subCatName
        val childAdapter = ChildAdapter(list[position].products)
        holder.recylerList.apply {
            adapter = childAdapter
        }

        childAdapter.onItemClick = {
            onItemClick?.invoke(it)
        }

    }

    override fun getItemCount(): Int {
        return  list.size
    }
}