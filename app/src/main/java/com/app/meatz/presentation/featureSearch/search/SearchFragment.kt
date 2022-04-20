package com.app.meatz.presentation.featureSearch.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.*
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentSearchBinding
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.app.meatz.presentation.featureSearch.SearchViewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val shopRecommandationsRvAdapter by lazy { ShopRecommandationsRvAdapter() }
    private val itemRecommandationsRvAdapter by lazy { ItemRecommandationRvAdapter() }
    private val viewModel by viewModels<SearchViewModel>()
    private var keyWord = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShopsRecommandationsRv()
        initItemsRecommandationsRv()
        handleViewsClicks()
        getSearcRecommandations()

    }


    private fun getSearcRecommandations() {
        keyWord = binding.sv.text.toString()
        viewModel.search(keyWord, 1).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    shimmer.root.visible()
                    clRoot.gone()
                }
                SUCCESS -> {
                    binding.apply {
                        shimmer.root.gone()
                        clRoot.visible()
                    }
                    it?.data?.let {
                        if (it.stores.isEmpty() && it.products.isEmpty()) {
                            binding.apply {
                                emptyLayout.visible()
                                clShopsSection.gone()
                                clItemsSection.gone()
                            }
                        } else {
                            binding.emptyLayout.gone()
                            handleItemsRecommandations(it.products)
                            handleStoresRecommandations(it.stores)
                        }
                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.rvItemsRecommandation, it?.message.toString(), true)
                }
                FAILURE -> {
                    requireContext().hideKeyboard(requireView())
                    requireActivity().setSnackbar(binding.rvItemsRecommandation, it.message, false)

                }
            }
        })
    }

    private fun handleItemsRecommandations(items: List<ProductData>) {
        if (items.isEmpty())
            binding.clItemsSection.gone()
        else {
            binding.clItemsSection.visible()
            itemRecommandationsRvAdapter.fill(items)
            if (items.size <= 3) {
                binding.apply {
                    line2.gone()
                    tvMoreItems.gone()
                }
            } else {
                binding.apply {
                    line2.visible()
                    tvMoreItems.visible()
                }
            }

        }
    }

    private fun initItemsRecommandationsRv() {
        binding.rvItemsRecommandation.apply {
            linearLayoutManager()
            adapter = itemRecommandationsRvAdapter
        }
        itemRecommandationsRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()

            when (item.type) {
                ITEM_BOX_TYPE -> {
                    bundle.putInt(BOX_ID, item.id)
                    mainController.navigate(R.id.action_search_boxDetails, bundle)
                }
                ITEM_SPECIAL_BOX -> {
                    bundle.putInt(BOX_ID, item.id)
                    mainController.navigate(R.id.action_search_offerboxDetails, bundle)
                }
                else -> {
                    bundle.putInt(PRODUCT_ID, item.id)
                    mainController.navigate(R.id.action_search_productDetails, bundle)
                }
            }

        }
    }


    private fun handleStoresRecommandations(stores: List<StoreData>) {
        if (stores.isEmpty())
            binding.clShopsSection.gone()
        else {

            binding.clShopsSection.visible()
            shopRecommandationsRvAdapter.fill(stores)
            if (stores.size <= 3)
                binding.apply {
                    line1.gone()
                    tvMoreShops.gone()
                }
            else
                binding.apply {
                    line1.visible()
                    tvMoreShops.visible()
                }
        }
    }

    private fun initShopsRecommandationsRv() {
        binding.rvShopsRecommandation.apply {
            linearLayoutManager()
            adapter = shopRecommandationsRvAdapter
        }
        shopRecommandationsRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(SHOP_ID, item.id)
            mainController.navigate(R.id.action_search_shopDetails, bundle)
        }
    }


    private fun handleViewsClicks() {

        binding.apply {

            sv.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getSearcRecommandations()
                    true
                } else false
            })

            sv.apply {
                onChange {
                    getSearcRecommandations()
                }
            }

            tvMoreShops.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(SEARCH_KEY_WORD, keyWord)
                bundle.putBoolean(SEARCH_ITEMS_TAB_IS_SELECTED, false)
                mainController.navigate(R.id.action_search_searchResults, bundle)
            }
            tvMoreItems.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(SEARCH_KEY_WORD, keyWord)
                bundle.putBoolean(SEARCH_ITEMS_TAB_IS_SELECTED, true)
                mainController.navigate(R.id.action_search_searchResults, bundle)
            }

        }
    }
}