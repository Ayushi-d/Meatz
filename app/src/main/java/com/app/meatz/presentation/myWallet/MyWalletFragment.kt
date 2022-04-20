package com.app.meatz.presentation.myWallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.PAYMENT_URL
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.GridSpacingItemDecoration
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentMyWalletBinding
import com.app.meatz.presentation.home.MainActivity

class MyWalletFragment : BaseFragment<FragmentMyWalletBinding>() {
    private val viewModel by viewModels<MyWalletViewModel>()
    private val walletCardsAdapter by lazy { WalletCardsRvAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyWallet()
        initWalletCardsRv()
        initViewsClicks()
    }

    private fun getMyWallet() {
        viewModel.getMyWallet().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        lnRoot.gone()
                        shimmer.root.visible()
                    }
                SUCCESS -> {
                    binding.apply {
                        lnRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        binding.tvWallet.text = getString(R.string.global_currency, it.wallet)
                        if (it.walletCardData.isNotEmpty())
                            walletCardsAdapter.fill(it.walletCardData)
                    }

                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.rvWalletCards, it.message.toString(), true)
                }
                FAILURE -> {
                    binding.lnRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initWalletCardsRv() {
        val gridLayout = GridLayoutManager(requireContext(), 2)
        binding.apply {
            rvWalletCards.apply {
                layoutManager = gridLayout
                adapter = walletCardsAdapter
                if (rvWalletCards.itemDecorationCount == 0)
                    rvWalletCards.addItemDecoration(GridSpacingItemDecoration(2, 30, false))
            }
        }
        walletCardsAdapter.setOnClickListener { clickedView, item, _ ->
            if (clickedView.id == R.id.tvRecharge)
                chargeWalletWithCardId(item.id)
        }

    }

    private fun chargeWalletWithCardId(cardId: Int) {
        viewModel.chargeWalletWithCards(cardId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.paymentUrl?.let {
                        val bundle = Bundle()
                        bundle.putString(PAYMENT_URL, it)
                        mainController.navigate(R.id.action_myWallet_payment, bundle)
                        binding.etRecharge.setText("")
                    }
                }
                FAILURE, ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.rvWalletCards, it.message.toString(), true)
                }

            }
        })
    }

    private fun chargeWalletWithAmount() {
        val rechargeAmount = binding.etRecharge.text?.trim().toString()
        viewModel.chargeWalletWithAmount(rechargeAmount).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.paymentUrl?.let {
                        val bundle = Bundle()
                        bundle.putString(PAYMENT_URL, it)
                        mainController.navigate(R.id.action_myWallet_payment, bundle)
                        binding.etRecharge.setText("")
                    }
                }
                FAILURE, ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.rvWalletCards, it.message.toString(), true)
                }

            }
        })
    }

    private fun initViewsClicks() {
        binding.tvRecharge.setOnClickListener {
            if (binding.etRecharge.text?.trim().toString().isEmpty()) {
                requireActivity().setSnackbar(binding.rvWalletCards, getString(R.string.myWallet_please_enter_price), true)
                binding.etRecharge.requestFocus()
            } else
                chargeWalletWithAmount()
        }
    }

}