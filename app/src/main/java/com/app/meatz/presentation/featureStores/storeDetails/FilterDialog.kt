package com.app.meatz.presentation.featureStores.storeDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.databinding.DialogFilterBinding
import com.app.meatz.domain.remote.Category
import com.app.meatz.presentation.featureStores.storeDetails.adapter.FilterCategoryRvAdapter

class FilterDialog(context: Context) : AppCompatDialog(context) {

    private val binding = DialogFilterBinding.inflate(layoutInflater)
    val filterSelectedCategory: ArrayList<Int> by lazy { ArrayList() }
    private val filterCategoriesAdapter by lazy { FilterCategoryRvAdapter() }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            initCategoriesRv()
        }
    }

    fun initCategoriesRv() {
        binding.rvFilterCategory.apply {
            linearLayoutManager()
            adapter = filterCategoriesAdapter
        }
        filterCategoriesAdapter.setOnClickListener { _, item, _ ->
            if (item.isSelected)
                filterSelectedCategory.add(item.id)
            else
                filterSelectedCategory.remove(item.id)
        }
    }

    fun setCategoriesList(_categoriesList: ArrayList<Category>) {
        filterCategoriesAdapter.fill(_categoriesList)
    }

    fun performSubmit(action: () -> Unit = {}) {
        binding.btnSubmit.setOnClickListener {
            action.invoke()
        }
    }

}