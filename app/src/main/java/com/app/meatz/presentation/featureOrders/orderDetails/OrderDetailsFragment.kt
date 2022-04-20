package com.app.meatz.presentation.featureOrders.orderDetails

import android.annotation.SuppressLint
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
import com.app.meatz.databinding.FragmentOrderDetailsBinding
import com.app.meatz.domain.remote.orderDetails.OrderDetails
import com.app.meatz.domain.remote.orderDetails.Payment
import com.app.meatz.presentation.featureOrders.OrdersViewModel
import com.app.meatz.presentation.home.MainActivity


class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    private val viewModel by viewModels<OrdersViewModel>()
    private val orderProductsRvAdapter by lazy { OrderProdutsRvAdapter() }
    private var orderId = 0
    private var paymentMethod = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            orderId = it.getInt(ORDER_ID)
        }
        initOrderProductsRv()
        getOrderDetails()
        handleViewsClicks()
    }

    private fun getOrderDetails() {
        viewModel.getOrderDetails(orderId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.visible()
                    }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        paymentMethod = it.payment_method
                        orderProductsRvAdapter.fill(it.products)
                        initOrderViews(it)
                        initPaymentViews(it.payment)
                    }
                }
                ERROR -> {
                   binding.shimmer.root.gone()
                    binding.clRoot.visible()
                    requireActivity().setSnackbar(binding.cvOrderInfo, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.shimmer.root.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initOrderProductsRv() {
        binding.rvProducts.apply {
            linearLayoutManager()
            adapter = orderProductsRvAdapter
        }
    }

    private fun initPaymentViews(payment: Payment) {
        binding.apply {
            tvPaymentMethod.text = payment.paymentMethod
            tvProductsTotal.text = getString(R.string.global_currency, payment.subtotal)
            tvDeliveryCharge.text = getString(R.string.global_currency, payment.delivery)
            tvTotal.text = getString(R.string.global_currency, payment.total)
            if (payment.discount[0].toString() == "0")
                tvDiscount.text = getString(R.string.global_currency, payment.discount)
            else
                tvDiscount.text = getString(R.string.global_currency, "-${payment.discount}")
            if (payment.transaction_id == null) {
                tvTransactionId.text = getString(R.string.order_details_n_a)
                tvPaymentId.text = getString(R.string.order_details_n_a)
            } else {
                tvTransactionId.text = payment.transaction_id.toString()
                tvPaymentId.text = payment.payment_id.toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initOrderViews(orderDetails: OrderDetails) {
        binding.apply {
            tvStatus.text = orderDetails.status
            tvOrderId.text = "#${orderDetails.id}"
            tvOrderDate.text = orderDetails.createdAt
            tvDeliveryType.text = orderDetails.delivery.delivery_type

            orderDetails.delivery.date?.let {
                tvDeliveryDate.text = it
            }
            orderDetails.delivery.time?.let {
                tvDeliveryTime.text = it
            }
            if (orderDetails.canReorder == 1 && viewModel.isUserLogged())
                tvReorder.visible()

            if (orderDetails.can_cancel == 1)
                btnRequestCancelt.visible()
            else
                btnRequestCancelt.gone()

            if (viewModel.isUserLogged()) {
                val addressSummary = StringBuilder()
                addressSummary.append("${orderDetails.address.area.name} ${root.context.getString(R.string.myaddress_block)} ${orderDetails.address.address.block} ${root.context.getString(R.string.myaddress_street)} ${orderDetails.address.address.street}\n")
                addressSummary.append("${root.context.getString(R.string.myaddress_building)} ${orderDetails.address.address.house_number}")
                if (orderDetails.address.address.levelNo.isNotEmpty())
                    addressSummary.append(" ${root.context.getString(R.string.myaddress_floor)} ${orderDetails.address.address.levelNo}")
                tvDeliveryAdd.text = addressSummary
            } else
                tvDeliveryAdd.text = orderDetails.address.full_address

        }
    }

    private fun handleViewsClicks() {
        binding.apply {
            tvReorder.setOnClickListener {
                reorder()
            }
            btnRequestCancelt.setOnClickListener {
                cancelRequest()
            }
        }
    }

    private fun reorder() {
        viewModel.reOrder(orderId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        val bundle = Bundle()
                        bundle.putString(SUBTOTAL, it.subtotal)
                        mainController.navigate(R.id.action_orderDetails_checkout, bundle)
                    }
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.cvOrderInfo, it.message)
                }
            }
        })
    }

    private fun cancelRequest() {
        viewModel.cancelRequest(orderId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        paymentMethod = it.payment_method
                        orderProductsRvAdapter.fill(it.products)
                        initOrderViews(it)
                        initPaymentViews(it.payment)
                    }
                    showAlert(getString(R.string.order_details_request_sent), it.message,
                            getString(R.string.base_ok), {
                    }, setCancelable = true, stanchedOnTouch = true)
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnRequestCancelt, it.message)
                }
            }
        })
    }


}