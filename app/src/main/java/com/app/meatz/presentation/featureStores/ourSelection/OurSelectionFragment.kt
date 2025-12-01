package com.app.meatz.presentation.featureStores.ourSelection

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.SHOP_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.GridSpacingItemDecoration
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentOurSelectionBinding
import com.app.meatz.domain.remote.stores.StoreCategories
import com.app.meatz.presentation.featureStores.StoresViewModel
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.shared.StoressRvAdapter
import com.google.android.material.tabs.TabLayout


class OurSelectionFragment : BaseFragment<FragmentOurSelectionBinding>() {
    private val shopAdapter by lazy { StoressRvAdapter() }
    private val viewModel by viewModels<StoresViewModel>()
    private var selectedSubCategoryId: String = ""
    //private var categoryList  = ArrayList<StoreCategories>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
            initOurSelectionRv()
           // revokeRecalling {
                getOurSelection()
           // }
            setupTabs()
        }
    }

    private fun setupTabs(){
        binding.catTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }


    private fun getOurSelection() {
        viewModel.getOurSelectionStores(selectedSubCategoryId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    clRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    it?.data?.let {
                        binding.apply {
                            clRoot.visible()
                            shimmer.root.gone()
                        }
                        shopAdapter.fill(it.storeData)
                        //categoryList.addAll(it.categories)
//                        for (i in 0..it.categories.size){
//                            if (it.categories[i].subcategories.size > 0){
//                                for (j in 0..it.categories[i].subcategories.size){
//                                    binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText(it.categories[i].subcategories[j].name.en))
//                                }
//                            }
//                        }
                    }
                }
                ERROR -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.gone()
                    }
                    requireActivity().setSnackbar(binding.tvTitle, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.gone()
                    }
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initOurSelectionRv() {

        val gridlayout = GridLayoutManager(activity, 1)

        binding.rvOurSelection.apply {
            layoutManager = gridlayout
            adapter = shopAdapter
        }

        if (binding.rvOurSelection.itemDecorationCount == 0)
            binding.rvOurSelection.addItemDecoration(GridSpacingItemDecoration(1, 30, false))
        shopAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(SHOP_ID, item.id)
            mainController.navigate(R.id.action_ourSelection_shopDetails, bundle)
        }

    }
}