package com.app.meatz.presentation.offersBoxes

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.BOX_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.onChange
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentOffersBinding
import com.app.meatz.presentation.featureBoxes.ourBoxes.OurBoxesRvAdapter
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.offersBoxes.adapter.OfferCategoriesRvAdapter


class OffersBoxesFragment : BaseFragment<FragmentOffersBinding>() {
    private val ourBoxesAdapter by lazy { OurBoxesRvAdapter() }
    private val offerCategoriesAdapter by lazy { OfferCategoriesRvAdapter() }
    private var searchKeyWord: String = ""
    private var selectedCategoryId: String = ""
    private var isSearchApplied: Boolean = false
    private val viewModel by viewModels<OfferBoxesViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
            getOfferBoxes()
        }

        initCategoriesRv()
        initBoxesRv()
        handleViewsListeners()
    }

    private fun getOfferBoxes() {
        viewModel.getOfferBoxes(searchKeyWord, selectedCategoryId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        shimmer.root.visible()
                        lnRoot.gone()
                    }
                SUCCESS -> {
                    binding.apply {
                        shimmer.root.gone()
                        lnRoot.visible()
                    }
                    it?.data?.let {

                        if (offerCategoriesAdapter.getCurrentItems().isEmpty())
                            offerCategoriesAdapter.fill(it.categories)
                        if (it.boxs.isNotEmpty()) {
                            ourBoxesAdapter.fill(it.boxs)
                            binding.apply {
                                emptyLayout.gone()
                                rvOfferBoxes.visible()
                            }
                            if (isSearchApplied) {
                                binding.tvResultCount.apply {
                                    visible()
                                    text = getString(R.string.offer_boxes_search_results, it.boxs.size.toString())
                                }

                            } else
                                binding.tvResultCount.gone()
                        } else {

                            binding.apply {
                                emptyLayout.visible()
                                rvOfferBoxes.gone()
                                if (isSearchApplied) {
                                    binding.tvResultCount.apply {
                                        visible()
                                        text = getString(R.string.offer_boxes_search_results, it.boxs.size.toString())
                                    }

                                } else {
                                    binding.tvResultCount.gone()
                                    tvEmpty.text = getString(R.string.offer_boxes_empty)
                                }


                            }

                        }
                    }

                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.rvCategories, it.message.toString(), true)
                }
                FAILURE -> {
                    binding.lnRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initCategoriesRv() {
        binding.rvCategories.apply {
            linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = offerCategoriesAdapter
        }
        offerCategoriesAdapter.setOnClickListener { _, item, _ ->
            selectedCategoryId = item.id.toString()
            getOfferBoxes()
        }
    }

    private fun initBoxesRv() {
        binding.rvOfferBoxes.apply {
            linearLayoutManager()
            adapter = ourBoxesAdapter
        }
        ourBoxesAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(BOX_ID, item.id)
            mainController.navigate(R.id.action_offers_offersDetails, bundle)
        }
    }

    private fun handleViewsListeners() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.etSearch.text?.toString()?.trim()?.let {
                    if (it.isNotEmpty()) {
                        searchKeyWord = it
                        isSearchApplied = true
                    } else
                        searchKeyWord = ""
                    getOfferBoxes()
                }
                true
            } else {
                false
            }
        }
        binding.etSearch.onChange {
            if (it.isEmpty()) {
                searchKeyWord = ""
                isSearchApplied = false
                getOfferBoxes()
            }
        }
    }
}