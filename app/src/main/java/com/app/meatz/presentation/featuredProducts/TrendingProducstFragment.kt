package com.app.meatz.presentation.featuredProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentTrendingProductsBinding
import com.app.meatz.presentation.featureStores.StoresViewModel
import com.app.meatz.presentation.featureStores.storeDetails.adapter.StoreProductsRvAdapter
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.shared.StoressRvAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.application.SHOP_ID
import com.app.meatz.data.utils.GridSpacingItemDecoration

class TrendingProducstFragment : BaseFragment<FragmentTrendingProductsBinding>() {

    private val productAdapter by lazy { FeaturedProductsRvAdapter() }
    private val viewModel by viewModels<StoresViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
           initFeatureProductRv()
            // revokeRecalling {
            getFeaturedProducts()
            // }
        }
    }

    private fun getFeaturedProducts() {
        viewModel.getFeaturedProducts().observe(viewLifecycleOwner, Observer {
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
                        productAdapter.fill(it.featuredProducts)
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

    private fun initFeatureProductRv() {

        val gridlayout = GridLayoutManager(activity, 1)

        binding.rvTrendingProducts.apply {
            layoutManager = gridlayout
            adapter = productAdapter
        }

        if (binding.rvTrendingProducts.itemDecorationCount == 0)
            binding.rvTrendingProducts.addItemDecoration(GridSpacingItemDecoration(1, 30, false))
           productAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(PRODUCT_ID, item.id)
            mainController.navigate(R.id.productDetailsFragment, bundle)
        }

    }
}