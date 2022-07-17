package com.app.meatz.presentation.productDetails


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.domain.remote.generalResponse.ProductOptionItems
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

//
//class MultiOptionsRvAdapter : BaseAdapter<ItemOptionBinding, ProductOptionItems>() {
//
//    override fun setContent(binding: ItemOptionBinding, item: ProductOptionItems, position: Int) {
//        binding.apply {
//             //for (i in item){
//                 Log.d(TAG, "setContent: nameeee-----"+item.name)
//                     tvOptionPrice.text = root.context.getString(R.string.global_currency, ""+item.price)
//                     cbOption.text = item.name
//            // }
////             tvOptionPrice.text = root.context.getString(R.string.global_currency, ""+item.price)
////             cbOption.text = item.name
//            cbOption.setOnClickListener {
//                Log.d(TAG, "setContent: yeeee-----"+position.toString())
//                onViewClicked(it, item, position)
//            }
//        }
//    }
//}

class MultiOptionsRvAdapter () :
    RecyclerView.Adapter<MultiOptionsRvAdapter.ViewHolder>()  {

    var produt  : List<ProductOptionItems>? = null
    private val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
    private lateinit var mListner : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(view : View ,position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener){
        mListner = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiOptionsRvAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return MultiOptionsRvAdapter.ViewHolder(view,mListner)    }

    override fun onBindViewHolder(holder: MultiOptionsRvAdapter.ViewHolder, position: Int) {
        holder.ChildItemTitle.text = produt!!.get(position).name
        holder.childitemPrice.text = holder.itemView.context.getString(R.string.global_currency, df.format(produt!!.get(position).price).toString())
    }

    override fun getItemCount(): Int {
        return produt!!.size
    }

    class ViewHolder(ItemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        var ChildItemTitle = itemView.findViewById<TextView>(R.id.cbOption)
        var childitemPrice = itemView.findViewById<TextView>(R.id.tvOptionPrice)

        init {
            ChildItemTitle.setOnClickListener{
                listener.onItemClick(it,adapterPosition)
            }
        }
    }
}