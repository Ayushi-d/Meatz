package com.app.meatz.presentation.featureOrders.myOrders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.application.SUBTOTAL
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentMyOrdersBinding
import com.app.meatz.presentation.featureOrders.OrdersViewModel
import com.app.meatz.presentation.home.MainActivity


class MyOrdersFragment : BaseFragment<FragmentMyOrdersBinding>() {

    private val viewModel by viewModels<OrdersViewModel>()
    private val ordersRvAdapter by lazy { MyOrdersRvAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrders()
        initOrdersRv()
    }

    private fun getOrders() {
        viewModel.getOrders().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        rlRoot.gone()
                        shimmer.root.visible()
                    }
                SUCCESS -> {
                    binding.apply {
                        rlRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        if (it.isEmpty()) {
                            binding.apply {
                                emptyLayout.visible()
                                rvMyOrders.gone()
                            }
                        } else
                            ordersRvAdapter.fill(it)
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.rvMyOrders, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.shimmer.root.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initOrdersRv() {
        binding.rvMyOrders.apply {
            linearLayoutManager()
            adapter = ordersRvAdapter
        }

        ordersRvAdapter.setOnClickListener { itemview, item, _ ->
            when (itemview.id) {
                R.id.tvReorder -> {
                    reorder(item.id)
                }
                else -> {
                    val bundle = Bundle()
                    bundle.putInt(ORDER_ID, item.id)
                    mainController.navigate(R.id.action_orders_orderDetails, bundle)
                }
            }

        }

    }

    private fun reorder(orderId: Int) {
        viewModel.reOrder(orderId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        val bundle = Bundle()
                        bundle.putString(SUBTOTAL, it.subtotal)
                        mainController.navigate(R.id.action_myOrders_checkout, bundle)
                    }
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.rvMyOrders, it.message)
                }
            }
        })
    }


}