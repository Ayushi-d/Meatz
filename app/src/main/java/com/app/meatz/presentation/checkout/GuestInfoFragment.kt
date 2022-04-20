package com.app.meatz.presentation.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.ADDRESS_OBJ
import com.app.meatz.data.application.SUBTOTAL
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentGuestInfoBinding
import com.app.meatz.domain.local.GuestAddress
import com.app.meatz.domain.remote.area.Governorates
import com.app.meatz.presentation.featureAddress.areas.AreaAdapter
import com.app.meatz.presentation.featureAddress.areas.GovernorateDialog
import com.app.meatz.presentation.home.MainActivity


class GuestInfoFragment : BaseFragment<FragmentGuestInfoBinding>() {

    private val viewModel by viewModels<CheckoutViewModel>()
    private lateinit var govList: List<Governorates>
    private var governorateDialog: GovernorateDialog? = null
    private var selectedAreaId = ""
    private var subTotal = ""
    private var delivery = ""
    private var expressDelivery = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            subTotal = it.getString(SUBTOTAL) ?: ""
        }
        handleViewsClick()
        getAreas()
        setObservers()
    }

    private fun setObservers() {
        AreaAdapter.selectedArea.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                selectedAreaId = it.id.toString()
                delivery = it.delivery.toString()
                expressDelivery = it.delivery_express.toString()
                binding.tvArea.text = it.name
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AreaAdapter.selectedArea.removeObservers(viewLifecycleOwner)
        AreaAdapter.selectedArea.value = null
    }

    private fun handleViewsClick() {
        binding.apply {
            flArea.setOnClickListener {
                if (governorateDialog == null) {
                    governorateDialog = GovernorateDialog(
                            requireContext(),
                            govList
                    )
                }

                governorateDialog?.initDialog()
            }
            btnContinue.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs()) {
                    val guestAddress = GuestAddress(tvArea.text.toString(),
                            selectedAreaId, etAddressName.text.toString(), getAddressSummary(),
                            etCustomerName.text.toString(),
                            etEmail.text.toString(),
                            etPhone.text.toString(),
                            delivery,
                            expressDelivery

                    )
                    val bundle = Bundle()
                    bundle.putParcelable(ADDRESS_OBJ, guestAddress)
                    bundle.putString(SUBTOTAL, subTotal)
                    mainController.navigate(R.id.action_guestInfo_checkout, bundle)
                }
            }
        }
    }

    private fun getAddressSummary(): String {
        binding.apply {
            val addressSummary = StringBuilder()
            addressSummary.append("${tvArea.text} ${getString(R.string.myaddress_block)} ${etBlock.text.toString()} ${getString(R.string.myaddress_street)} ${etStreet.text.toString()} \n ")
            addressSummary.append("${getString(R.string.myaddress_building)} ${etHouseNumber.text.toString()}")
            if (etFloor.text.toString().isNotEmpty())
                addressSummary.append(" ${getString(R.string.myaddress_floor)} ${etFloor.text.toString()}")
            return addressSummary.toString()
        }
    }

    private fun validateInputs(): Boolean {

        binding.apply {
            if (etCustomerName.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.guest_info_validate_customer_name), true)
                etCustomerName.requestFocus()
                return false
            } else if (!etEmail.isEmpty() && !etEmail.isEmailValid()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.validation_invalid_email), true)
                etEmail.requestFocus()
                return false
            } else if (etPhone.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.validation_empty_phone), true)
                etPhone.requestFocus()
                return false
            } else if (!etPhone.isPhoneValid()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.validation_invalid_phone), true)
                etPhone.requestFocus()
                return false
            } else if (etAddressName.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.add_address_empty_address_name), true)
                etPhone.requestFocus()
                return false
            } else if (selectedAreaId.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.add_address_empty_area), true)
                return false
            } else if (etBlock.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.add_address_empty_block), true)
                etBlock.requestFocus()
                return false
            } else if (etStreet.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.add_address_empty_street), true)
                etStreet.requestFocus()
                return false
            } else if (etHouseNumber.isEmpty()) {
                requireActivity().setSnackbar(binding.btnContinue, getString(R.string.add_address_empty_house), true)
                etHouseNumber.requestFocus()
                return false
            }
            return true
        }

    }


    private fun getAreas() {
        viewModel.getAreas().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    clRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    govList = ArrayList()
                    it?.data?.let {
                        govList = it
                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.btnContinue, it.message, true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

}