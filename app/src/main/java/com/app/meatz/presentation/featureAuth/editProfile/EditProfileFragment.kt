package com.app.meatz.presentation.featureAuth.editProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.extensions.isPhoneValid
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentEditProfileBinding
import com.app.meatz.presentation.featureAuth.AuthViewModel


class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    private val viewModel by viewModels<AuthViewModel>()
    private val hashmap: HashMap<String, Any> by lazy { HashMap() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewsData()
        handleViewsClicks()
    }

    private fun setViewsData() {
        binding.apply {
            etFName.setText(viewModel.getUser()?.firstName)
            etLName.setText(viewModel.getUser()?.lastName)
            etPhone.setText(viewModel.getUser()?.mobile)

        }
    }

    private fun validateInputs(): Boolean {
        binding.apply {
            if (etFName.isEmpty()) {
                requireActivity().setSnackbar(binding.btnUpate, getString(R.string.validation_empty_first_name), true)
                etFName.requestFocus()
                return false
            } else if (etLName.isEmpty()) {
                requireActivity().setSnackbar(binding.btnUpate, getString(R.string.validation_empty_last_name), true)
                etLName.requestFocus()
                return false
            } else if (etPhone.isEmpty()) {
                requireActivity().setSnackbar(binding.btnUpate, getString(R.string.validation_empty_phone), true)
                etPhone.requestFocus()
                return false
            } else if (!etPhone.isPhoneValid()) {
                requireActivity().setSnackbar(binding.btnUpate, getString(R.string.validation_invalid_phone), true)
                etPhone.requestFocus()
                return false
            }
            return true


        }
    }

    private fun handleViewsClicks() {
        binding.apply {
            btnUpate.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    editProfile()

            }
        }
    }

    private fun fillEditProfileHashmap() {
        hashmap.clear()
        binding.apply {
            hashmap["first_name"] = etFName.text?.trim().toString()
            hashmap["last_name"] = etLName.text?.trim().toString()
            hashmap["mobile"] = etPhone.text?.trim().toString()
            hashmap["email"] = viewModel.getUser()?.email.toString()
        }

    }

    private fun editProfile() {
        fillEditProfileHashmap()
        viewModel.editProfile(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    if (it?.data != null) {
                        viewModel.cacheUserData(it.data)
                        showAlert(getString(R.string.edit_profile_profile_update), it.message,
                                getString(R.string.edit_profile_ok), {
                            mainController.popBackStack()
                        }
                        )
                    } else
                        requireActivity().setSnackbar(binding.btnUpate, it.message, true)

                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnUpate, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnUpate, it.message)
                }
            }
        })
    }
}