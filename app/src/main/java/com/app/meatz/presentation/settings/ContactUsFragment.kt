package com.app.meatz.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.MEATZ_WHATSAPP
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentContactUsBinding


class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

    private val viewModel by viewModels<SettingsViewModel>()
    private var whatsapp = ""
    private val contactUsHashMap by lazy { HashMap<String, String>() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            whatsapp = it.getString(MEATZ_WHATSAPP) ?: ""
            if (whatsapp.isEmpty())
                binding.cvWhatsapp.gone()
        }
        handleViewsClick()
    }

    private fun validateInputs(): Boolean {
        binding.apply {
            if (etName.isEmpty()) {
                etName.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.contactus_validate_name_empty), true)
                return false
            } else if (etEmail.isEmpty()) {
                etEmail.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.validation_empty_email), true)
                return false
            } else if (!etEmail.isEmailValid()) {
                etEmail.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.validation_invalid_email), true)
                return false
            } else if (etPhone.isEmpty()) {
                etPhone.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.validation_empty_phone), true)
                return false

            } else if (!etPhone.isPhoneValid()) {
                etPhone.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.validation_invalid_phone), true)
                return false
            } else if (etMessage.isEmpty()) {
                etMessage.requestFocus()
                requireActivity().setSnackbar(binding.btnSend, getString(R.string.contactus_validate_message_empty), true)
                return false
            }
            return true
        }
    }

    private fun handleViewsClick() {

        binding.apply {
            cvWhatsapp.setOnClickListener {
                requireActivity().openSocialLink("whatsapp", whatsapp)
            }
            btnSend.setOnClickListener {
                if (validateInputs())
                    sendMessage()
            }
        }
    }

    private fun fillContactusMap() {
        contactUsHashMap.clear()
        binding.apply {
            contactUsHashMap["name"] = etName.text.toString()
            contactUsHashMap["email"] = etEmail.text.toString()
            contactUsHashMap["mobile"] = etPhone.text.toString()
            contactUsHashMap["message"] = etMessage.text.toString()
        }

    }

    private fun sendMessage() {
        fillContactusMap()
        viewModel.sendMessage(contactUsHashMap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    showAlert(getString(R.string.contactus_thanks), it.message, getString(R.string.edit_profile_ok),
                            {
                                mainController.popBackStack()
                            }, true, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnSend, it.message)
                }
            }
        })
    }


}