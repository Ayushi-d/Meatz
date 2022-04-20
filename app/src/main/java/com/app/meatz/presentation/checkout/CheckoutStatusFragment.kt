package com.app.meatz.presentation.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.CHECKOUT_STATUS_OBJ
import com.app.meatz.data.preferences.IsOrderSuccess
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.roundPrice
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.FragmentCheckoutStatusBinding
import com.app.meatz.domain.local.CheckoutStatusRsm
import kotlin.math.roundToInt


class CheckoutStatusFragment : BaseFragment<FragmentCheckoutStatusBinding>() {
    private lateinit var checkoutStatus: CheckoutStatusRsm


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<CheckoutStatusRsm>(CHECKOUT_STATUS_OBJ)?.let {
            checkoutStatus = it
        }
        if (checkoutStatus.orderId == 0) {
            if (checkoutStatus.isPaymentSuccess)
                successWalletPayment()
            else
                failWalletPayment()
        } else {
            if (checkoutStatus.isPaymentSuccess) {
                IsOrderSuccess = true
                if (isUser())
                    handleVIewsInCaseLogged()
                else
                    handleViewsInCaseGuest()
            } else {
                IsOrderSuccess = false
                failOrder()
            }
        }
    }

    private fun failOrder() {
        binding.apply {

                btnmyOrders.apply {
                    text = getString(R.string.checkout_status_try_again_btn)
                    setOnClickListener {
                        mainController.popBackStack(R.id.checkoutFragment, false)
                    }
                }


            lnTransactionId.gone()
            lnOrderId.gone()
            lnPaymentId.gone()
            tvOrderStatus.text = getString(R.string.checkout_status_error)
            ivOrderStatus.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_order_invalid))
            tvOrderfailMsg.visible()

        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleViewsInCaseGuest() {
        binding.apply {
            tvOrderid.text = "#${checkoutStatus.orderId}"
            lnTransactionId.gone()
            lnPaymentId.gone()
            btnmyOrders.text = getString(R.string.main_home)
            btnmyOrders.setOnClickListener {
                mainController.popBackStack(R.id.homeFragment, false)
            }
            handlePaymentViews()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleVIewsInCaseLogged() {
        binding.apply {
            tvOrderid.text = "#${checkoutStatus.orderId}"
            btnmyOrders.apply {
                setOnClickListener { mainController.navigate(R.id.action_checkoutStatus_myOrders) }
                text = getString(R.string.checkout_status_myorders)
            }
            handlePaymentViews()
        }
    }

    @SuppressLint("SetTextI18n")
    fun handlePaymentViews() {

        binding.apply {

            binding.apply {
                tvOrderid.visible()
                tvOrderid.text = "#${checkoutStatus.orderId}"
            }
            if (checkoutStatus.isCashedPayment) {
                lnPaymentId.gone()
                lnTransactionId.gone()
            } else {
                binding.apply {
                    lnPaymentId.visible()
                    lnTransactionId.visible()
                    tvPaymentId.text = checkoutStatus.paymentId.toString()
                    tvTransactionId.text = checkoutStatus.transactionId.toString()
                }
            }
        }
    }

    private fun successWalletPayment() {
        binding.apply {
            tvOrderid.text = getString(R.string.global_currency, roundPrice(checkoutStatus.paymentAmount.toDouble().roundToInt()))
            txtOrderId.text = getString(R.string.myWallet_recharge_amount)
            tvOrderStatus.text = getString(R.string.myWallet_recharge_successfully)
            btnmyOrders.text = getString(R.string.myWallet)
            btnmyOrders.setOnClickListener {
                mainController.popBackStack(R.id.myWalletFragment, false)
            }
            lnPaymentId.visible()
            lnTransactionId.visible()
            tvPaymentId.text = checkoutStatus.paymentId
            tvTransactionId.text = checkoutStatus.transactionId
        }
    }

    private fun failWalletPayment() {

        binding.apply {
            btnmyOrders.apply {
                text = getString(R.string.checkout_status_try_again_btn)
                setOnClickListener {
                    mainController.popBackStack(R.id.paymentFragment, false)
                }
            }


            lnTransactionId.gone()
            lnOrderId.gone()
            lnPaymentId.gone()
            tvOrderStatus.text = getString(R.string.checkout_status_error)
            ivOrderStatus.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_order_invalid))
            tvOrderfailMsg.visible()
        }
    }
}

