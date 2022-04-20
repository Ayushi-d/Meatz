package com.app.meatz.presentation.productDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.databinding.DialogMyBoxBinding
import com.app.meatz.domain.remote.MyBox

class MyBoxesDialog(context: Context) : AppCompatDialog(context) {
    private val binding = DialogMyBoxBinding.inflate(layoutInflater)
    private var selectedBox: ArrayList<Int> = ArrayList()

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }


    }

    fun showDialog(list: List<MyBox>, action: () -> Unit = {}) {
        val myboxesRvAdapter by lazy { MyBoxesRvAdapter() }
        myboxesRvAdapter.fill(list)
        binding.ivClose.setOnClickListener { dismiss() }
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            btnAdd.setOnClickListener {
                if (selectedBox.isNotEmpty())
                    action.invoke()
                dismiss()
            }
        }
        binding.rvMyBoxes.apply {
            linearLayoutManager()
            adapter = myboxesRvAdapter
        }


        myboxesRvAdapter.setOnClickListener { itemview, item, _ ->
            if (itemview.id == R.id.cbMybox) {
                if (item.isChecked)
                    selectedBox.add(item.id)
                else
                    selectedBox.remove(item.id)
            }
        }
        this.show()
    }

    fun getSelectedBox(): List<Int> = selectedBox
}