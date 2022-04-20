package com.app.meatz.presentation.featureAuth.changePassword

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
import com.app.meatz.data.preferences.removeUser
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.extensions.isPasswordValid
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentChangePasswordBinding
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.featureAuth.AuthViewModel
import org.jetbrains.anko.support.v4.intentFor

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    private val viewModel by viewModels<AuthViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewsClicks()
    }

    private fun validateInputs(): Boolean {
        binding.apply {
            if (etOldPass.isEmpty()) {
                requireActivity().setSnackbar(btnChange, getString(R.string.change_password_old_pass_empty), true)
                etOldPass.requestFocus()
                return false
            } else if (etNewPass.isEmpty()) {
                requireActivity().setSnackbar(btnChange, getString(R.string.change_password_new_pass_empty), true)
                etNewPass.requestFocus()
                return false
            } else if (!etNewPass.isPasswordValid()) {
                requireActivity().setSnackbar(btnChange, getString(R.string.validation_invalid_password), true)
                etNewPass.requestFocus()
                return false
            } else if (etConfirmPass.isEmpty()) {
                requireActivity().setSnackbar(btnChange, getString(R.string.change_password_confirmed_new_pass_empty), true)
                etConfirmPass.requestFocus()
                return false
            } else if (etConfirmPass.text.toString() != etNewPass.text.toString()) {
                requireActivity().setSnackbar(btnChange, getString(R.string.validation_password_not_match), true)
                etConfirmPass.requestFocus()
                return false
            }
            return true
        }
    }

    private fun changePassword() {
        viewModel.changePassword(binding.etOldPass.text.toString(), binding.etNewPass.text.toString()).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    showAlert(
                            getString(R.string.change_password_update), it.message,
                            getString(R.string.login),
                            {
                                removeUser()
                                startActivity(intentFor<AuthActivity>())
                            },
                            hideCloseBtn = true,
                    )
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnChange, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnChange, it.message)
                }
            }
        })
    }

    private fun handleViewsClicks() {
        binding.apply {
            btnChange.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    changePassword()
            }
        }
    }
}