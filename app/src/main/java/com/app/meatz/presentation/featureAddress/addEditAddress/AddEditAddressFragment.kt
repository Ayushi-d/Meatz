package com.app.meatz.presentation.featureAddress.addEditAddress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.ADDRESS_OBJ
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentAddEditAddressBinding
import com.app.meatz.domain.remote.address.Address
import com.app.meatz.domain.remote.address.AddressData
import com.app.meatz.domain.remote.area.Governorates
import com.app.meatz.presentation.featureAddress.AddressViewModel
import com.app.meatz.presentation.featureAddress.areas.AreaAdapter
import com.app.meatz.presentation.featureAddress.areas.GovernorateDialog
import com.app.meatz.presentation.home.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AddEditAddressFragment : BaseFragment<FragmentAddEditAddressBinding>() {

    private val viewModel by viewModels<AddressViewModel>()
    private val addressHashmap: HashMap<String, Any> by lazy { HashMap() }
    private lateinit var govList: List<Governorates>
    private var governorateDialog: GovernorateDialog? = null
    private var selectedAreaId = ""
    private var address: Address? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            address = it.getParcelable<Address>(ADDRESS_OBJ)
            setAddressView()
        }
        getAreas()
        handleViewsClicks()
        setObservers()
    }

    private fun setAddressView() {
        address?.let { address ->
            binding.apply {
                tvTitle.text = getString(R.string.edit_address)
                btnAdd.text = getString(R.string.edit_address)
                etAddressName.setText(address.addressName)
                tvArea.text = address.area.name
                selectedAreaId = address.area.id.toString()
                etBlock.setText(address.address.block)
                etStreet.setText(address.address.street)
                etHouseNumber.setText(address.address.house_number)
                etApartment.setText(address.address.apartment_no)
                etFloor.setText(address.address.levelNo)
                etNotes.setText(address.address.notes)
            }
        }
    }

    private fun handleViewsClicks() {
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
            btnAdd.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    if (address == null)
                        addAddress()
                    else {
                        val addressObj = AddressData(
                                etAddressName.text.toString(),
                                selectedAreaId,
                                etBlock.text.toString(),
                                etHouseNumber.text.toString(),
                                etApartment.text.toString(),
                                etFloor.text.toString(),
                                etNotes.text.toString(),
                                etStreet.text.toString()
                        )
                        editAddress(addressObj)

                    }
            }
        }

    }

    private fun fillAddressMap() {
        addressHashmap.clear()
        binding.apply {
            addressHashmap["address_name"] = etAddressName.text.toString()
            addressHashmap["area_id"] = selectedAreaId
            addressHashmap["block"] = etBlock.text.toString()
            addressHashmap["street"] = etStreet.text.toString()
            addressHashmap["house_number"] = etHouseNumber.text.toString()
            addressHashmap["apartment_no"] = etApartment.text.toString()
            addressHashmap["level_no"] = etFloor.text.toString()
            addressHashmap["notes"] = etNotes.text.toString()
        }

    }

    private fun addAddress() {
        fillAddressMap()
        viewModel.addAddress(addressHashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                    viewLifecycleOwner.lifecycle.coroutineScope.launch {
                        delay(500)
                        mainController.popBackStack()
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAdd, it.message)
                }
            }
        })
    }

    private fun editAddress(addressData: AddressData) {
        fillAddressMap()
        address?.id?.let {
            viewModel.editAddress(addressData, it).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    LOADING -> showLoading()
                    SUCCESS -> {
                        dismissLoading()
                        requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                        viewLifecycleOwner.lifecycle.coroutineScope.launch {
                            delay(500)
                            mainController.popBackStack()
                        }
                    }
                    ERROR -> {
                        dismissLoading()
                        requireActivity().setSnackbar(binding.btnAdd, it?.message.toString(), true)
                    }
                    FAILURE -> {
                        dismissLoading()
                        requireActivity().setSnackbar(binding.btnAdd, it.message)
                    }
                }
            })
        }

    }


    private fun validateInputs(): Boolean {
        binding.apply {
            if (etAddressName.isEmpty()) {
                requireActivity().setSnackbar(binding.btnAdd, getString(R.string.add_address_empty_address_name), true)
                etAddressName.requestFocus()
                return false
            } else if (selectedAreaId.isEmpty()) {
                requireActivity().setSnackbar(binding.btnAdd, getString(R.string.add_address_empty_area), true)
                return false
            } else if (etBlock.isEmpty()) {
                requireActivity().setSnackbar(binding.btnAdd, getString(R.string.add_address_empty_block), true)
                etBlock.requestFocus()
                return false
            } else if (etStreet.isEmpty()) {
                requireActivity().setSnackbar(binding.btnAdd, getString(R.string.add_address_empty_street), true)
                etStreet.requestFocus()
                return false
            } else if (etHouseNumber.isEmpty()) {
                requireActivity().setSnackbar(binding.btnAdd, getString(R.string.add_address_empty_house), true)
                etHouseNumber.requestFocus()
                return false
            }
            return true

        }
    }

    private fun setObservers() {
        AreaAdapter.selectedArea.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                selectedAreaId = it.id.toString()
                binding.tvArea.text = it.name
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        AreaAdapter.selectedArea.removeObservers(viewLifecycleOwner)
        AreaAdapter.selectedArea.value = null
    }

    private fun getAreas() {
        viewModel.getAreas().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.visible()
                    }
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
                    requireActivity().setSnackbar(binding.btnAdd, it.message, true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }


}