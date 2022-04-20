package com.app.meatz.presentation.featureAuth.forgetPassword

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
import com.app.meatz.data.utils.extensions.isEmailValid
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentForgetPasswordBinding
import com.app.meatz.presentation.featureAuth.AuthViewModel


class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    private val viewmodel by viewModels<AuthViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewsClicks()
    }

    private fun forgetPassword() {
        viewmodel.forgetPassword(binding.etEmail.text.toString()).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    if (it?.data != null)
                        showAlert(getString(R.string.forgot_password_sent), it.message,
                                getString(R.string.edit_profile_ok), { authController.popBackStack(R.id.loginFragment, false) }, setCancelable = false,
                                stanchedOnTouch = false)
                    else
                        requireActivity().setSnackbar(binding.btnSendPass, it.message, true)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnSendPass, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnSendPass, it.message)
                }
            }
        })
    }

    private fun handleViewsClicks() {
        binding.apply {
            btnSendPass.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    forgetPassword()
            }
            ivBack.setOnClickListener {
                requireContext().hideKeyboard(it)
                authController.popBackStack()
            }
        }
    }


    private fun validateInputs(): Boolean {
        binding.apply {
            if (etEmail.isEmpty()) {
                requireActivity().setSnackbar(binding.btnSendPass, getString(R.string.validation_empty_email), true)
                etEmail.requestFocus()
                return false
            } else if (!etEmail.isEmailValid()) {
                requireActivity().setSnackbar(binding.btnSendPass, getString(R.string.validation_invalid_email), true)
                etEmail.requestFocus()
                return false
            }
            return true
        }
    }
}