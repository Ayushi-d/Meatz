package com.app.meatz.presentation.featureAddress.myAddress

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.ADDRESS_OBJ
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentAddressBinding
import com.app.meatz.domain.remote.address.Address
import com.app.meatz.presentation.featureAddress.AddressViewModel
import com.app.meatz.presentation.home.MainActivity


class AddressFragment : BaseFragment<FragmentAddressBinding>() {
    private lateinit var addressRvAdapter: AddressRvAdapter
    private val viewModel by viewModels<AddressViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAddress()
        handleViewsClicks()
    }


    fun getAddress() {
        viewModel.getAddress().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    rlRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    binding.apply {
                        rlRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        handleAddressView(it)
                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun handleAddressView(list: List<Address>) {
        if (list.isEmpty()) {
            binding.apply {
                lnemptyLayout.visible()
                rvAddress.gone()
                tvItemsNumber.text = "0 ${getString(R.string.myaddress_saved_address)}"
            }
        } else {
            addressRvAdapter = AddressRvAdapter()
            binding.apply {
                lnemptyLayout.gone()
                rvAddress.visible()
                tvItemsNumber.text = "${list.size} ${getString(R.string.myaddress_saved_address)}"
                addressRvAdapter.fill(list)
                initAddressRv()
            }
        }
    }

    private fun initAddressRv() {
        binding.rvAddress.apply {
            linearLayoutManager()
            adapter = addressRvAdapter
        }

        addressRvAdapter.setOnClickListener { itemview, item, _ ->
            when (itemview.id) {
                R.id.btnEdit -> {
                    val bundle = Bundle()
                    bundle.putParcelable(ADDRESS_OBJ, item)
                    mainController.navigate(R.id.action_addresses_addAddress, bundle)
                }
                R.id.flRemove -> {
                    showAlert(getString(R.string.myaddress_delete_address), getString(R.string.myaddress_delete_address_msg),
                            getString(R.string.base_ok), {
                        deleteAddress(item.id)
                    }, true, true)
                }
            }
        }
    }

    private fun deleteAddress(addressId: Int) {
        viewModel.deleteAddress(addressId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let { addressList ->
                        requireActivity().setSnackbar(binding.btnAdd, it.message, true)
                        handleAddressView(addressList)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAdd, it.message, false)
                }
            }
        })
    }

    private fun handleViewsClicks() {
        binding.btnAdd.setOnClickListener {
            mainController.navigate(R.id.action_addresses_addAddress)
        }
    }

}