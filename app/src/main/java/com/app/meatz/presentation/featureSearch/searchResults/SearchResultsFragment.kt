package com.app.meatz.presentation.featureSearch.searchResults

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.*
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.GridSpacingItemDecoration
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentSearchResultsBinding
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.app.meatz.presentation.featureSearch.SearchViewModel
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.shared.StoressRvAdapter
import com.google.android.material.tabs.TabLayout

class SearchResultsFragment : BaseFragment<FragmentSearchResultsBinding>() {

    private var keyword = ""
    private var isItemTabSelectedByDefault = false
    private val viewModel by viewModels<SearchViewModel>()
    private val storesRvAdapter by lazy { StoressRvAdapter() }
    private val productsRvAdapter by lazy { ProductSearchResultsRvAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            keyword = it.getString(SEARCH_KEY_WORD) ?: ""
            isItemTabSelectedByDefault = it.getBoolean(SEARCH_ITEMS_TAB_IS_SELECTED)

        }
        initTabs()

        getSearchResults()

    }


    private fun getSearchResults() {
        viewModel.search(keyword, 1).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.visible()
                    }
                    if (isItemTabSelectedByDefault) {
                        binding.apply {
                            shimmer.shimmerLnProducts.visible()
                            shimmer.shimmerLnStores.gone()
                        }
                    }
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        handleTabs(it.stores, it.products)
                    }
                }
                ERROR -> {
                  binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.emptyLayout, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.shimmer.root.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initTabs() {
        binding.tabSearchResults.apply {
            removeAllTabs()
            addTab(binding.tabSearchResults.newTab().setText(getString(R.string.search_results_shops)), 0)
            addTab(binding.tabSearchResults.newTab().setText(getString(R.string.search_results_items)), 1)
        }

    }

    private fun handleTabs(storesList: List<StoreData>, itemList: List<ProductData>) {
        storesRvAdapter.fill(storesList)
        productsRvAdapter.fill(itemList)
        if (isItemTabSelectedByDefault) {
            handleRecycleView(1)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tabSearchResults.getTabAt(1)?.select()
            }, 40)
        } else {
            handleRecycleView(0)

            Handler(Looper.getMainLooper()).postDelayed({
                binding.tabSearchResults.getTabAt(0)?.select()
            }, 40)
        }


        binding.tabSearchResults.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { handleRecycleView(it) }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun handleRecycleView(position: Int) {

        if (position == 0)
            handleShopsList()
        else
            handleItemsShopList()

    }

    @SuppressLint("SetTextI18n")
    private fun handleShopsList() {
        if (storesRvAdapter.getCurrentItems().isNotEmpty()) {
            val count = storesRvAdapter.getCurrentItems().size
            binding.apply {
                rvShops.visible()
                rvItems.gone()
                emptyLayout.gone()
                tvTitle.visible()
                    tvTitle.text = "$count ${getString(R.string.search_results_shops_count)}"
            }
        } else {
            binding.apply {
                rvItems.gone()
                emptyLayout.visible()
                tvTitle.gone()
            }

        }

        val gridlayout = GridLayoutManager(activity, 1)
        binding.rvShops.apply {
            layoutManager = gridlayout
            adapter = storesRvAdapter
            if (this.itemDecorationCount == 0)
                this.addItemDecoration(GridSpacingItemDecoration(1, 30, false))
        }
        storesRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(SHOP_ID, item.id)
            mainController.navigate(R.id.action_searchResults_shopDetails, bundle)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun handleItemsShopList() {

        if (productsRvAdapter.getCurrentItems().isNotEmpty()) {
            val count = productsRvAdapter.getCurrentItems().size
            binding.apply {
                rvShops.gone()
                rvItems.visible()
                emptyLayout.gone()
                tvTitle.visible()
                    tvTitle.text = "$count ${getString(R.string.search_results_items_count)}"

            }
        } else {
            binding.apply {
                rvShops.gone()
                emptyLayout.visible()
                tvTitle.gone()
            }
        }

        val gridlayout = GridLayoutManager(activity, 2)
        binding.rvItems.apply {
            layoutManager = gridlayout
            adapter = productsRvAdapter
        }

        productsRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            if (item.type == ITEM_BOX_TYPE) {
                bundle.putInt(BOX_ID, item.id)
                mainController.navigate(R.id.action_searchResults_boxDetails, bundle)
            } else if (item.type == ITEM_SPECIAL_BOX) {
                bundle.putInt(BOX_ID, item.id)
                mainController.navigate(R.id.action_searchResults_offerBoxDetails, bundle)
            } else {
                bundle.putInt(PRODUCT_ID, item.id)
                mainController.navigate(R.id.action_searchResults_productDetails, bundle)
            }
        }


    }
}