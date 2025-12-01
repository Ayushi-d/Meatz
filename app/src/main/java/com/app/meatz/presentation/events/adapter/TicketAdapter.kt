package com.app.meatz.presentation.events.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.meatz.R
import com.app.meatz.domain.remote.CarnivalTickets
import com.app.meatz.domain.remote.address.Address

class TicketAdapter(spinnerDataList: ArrayList<CarnivalTickets>, context: Context) : BaseAdapter() {
    private val mContext: Context = context
    private val mSpinnerDataList: ArrayList<CarnivalTickets> = spinnerDataList


    override fun getCount(): Int {
        return mSpinnerDataList.size - 1
    }

    override fun getItem(position: Int): Any {
        return mSpinnerDataList[position]
    }


    override fun getItemId(position: Int): Long {
        return mSpinnerDataList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertViewVar = convertView
        if (convertViewVar == null) {
            val inflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewVar = inflater.inflate(R.layout.item_spinner, null)
        }

        val title = convertViewVar?.findViewById<TextView>(R.id.text1)
        title?.text = mSpinnerDataList[position].name

        return convertViewVar
    }

}