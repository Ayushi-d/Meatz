package com.app.meatz.presentation.featureAuth.register

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.FROM_REGISTER
import com.app.meatz.data.application.PAGE_ITEM
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentRegisterBinding
import com.app.meatz.presentation.featureAuth.AuthViewModel
import com.app.meatz.presentation.home.MainActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel by viewModels<AuthViewModel>()
    private val hashmap: HashMap<String, Any> by lazy { HashMap() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewsClicks()
        setSpannableText()

    }


    private fun handleViewsClicks() {
        hashmap.clear()
        binding.apply {
            btnRegister.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    signup()
            }
            tvSignin.setOnClickListener { authController.popBackStack() }
        }


    }

    private fun fillSignupData() {
        hashmap.clear()
        binding.apply {
            hashmap["first_name"] = etFirstname.text?.trim().toString()
            hashmap["last_name"] = etLastname.text?.trim().toString()
            hashmap["email"] = etEmail.text?.trim().toString()
            hashmap["mobile"] = etPhone.text?.trim().toString()
            hashmap["password"] = etPassword.text.toString()
        }
    }

    private fun signup() {
        fillSignupData()
        viewModel.signup(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    if (it?.data != null) {
                        viewModel.cacheUserData(it.data)
                        showAlert(getString(R.string.register_confirmation), it.message,
                                getString(R.string.edit_profile_ok), {
                            startActivity(intentFor<MainActivity>().clearTask().newTask())
                        }, setCancelable = false, stanchedOnTouch = false)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnRegister, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnRegister, it.message)
                }
            }
        })
    }

    private fun setSpannableText() {
        val ss = SpannableString("${getString(R.string.register_accept)} ${getString(R.string.register_privacy_policies)}${getString(R.string.register_terms_conditions)} ")
        val spPrivacyPolicy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                getPageDetails(3)
            }

            override fun updateDrawState(ds: TextPaint) {
                setClickableSpanStyle(ds)
            }
        }

        val termsConditions: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                getPageDetails(2)

            }

            override fun updateDrawState(ds: TextPaint) {
                setClickableSpanStyle(ds)
            }
        }

        ss.setSpan(spPrivacyPolicy, getString(R.string.register_accept).length + 1,
                getString(R.string.register_accept).length + getString(R.string.register_privacy_policies).length - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        ss.setSpan(termsConditions,
                getString(R.string.register_accept).length + getString(R.string.register_privacy_policies).length + 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvTerms.text = ss
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun setClickableSpanStyle(ds: TextPaint) {
        ds.color = ContextCompat.getColor(requireContext(), R.color.white)
        ds.isUnderlineText = false
    }

    private fun getPageDetails(pageId: Int) {
        viewModel.getPageDetails(pageId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        val bundle = Bundle()
                        bundle.putBoolean(FROM_REGISTER, true)
                        bundle.putParcelable(PAGE_ITEM, it)
                        authController.navigate(R.id.action_signUp_terms, bundle)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnRegister, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnRegister, it.message)
                }
            }
        })
    }

    private fun validateInputs(): Boolean {
        binding.apply {
            if (etFirstname.isEmpty()) {
                etFirstname.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_first_name), true)
                return false
            } else if (etLastname.isEmpty()) {
                etLastname.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_last_name), true)
                return false
            } else if (etEmail.isEmpty()) {
                etEmail.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_email), true)
                return false
            } else if (!etEmail.isEmailValid()) {
                etEmail.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_invalid_email), true)
                return false
            } else if (etPhone.isEmpty()) {
                etPhone.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_phone), true)
                return false
            } else if (!etPhone.isPhoneValid()) {
                etPhone.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_invalid_phone), true)
                return false
            } else if (etPassword.isEmpty()) {
                etPassword.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_password), true)
                return false
            } else if (!etPassword.isPasswordValid()) {
                etPassword.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_invalid_password), true)
                return false
            } else if (etPasswordConfirm.isEmpty()) {
                etPasswordConfirm.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_empty_confirmed_password), true)
                return false
            } else if (etPasswordConfirm.text.toString() != etPassword.text.toString()) {
                etPasswordConfirm.requestFocus()
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_password_not_match), true)
                return false
            } else if (!cbTerms.isChecked) {
                requireActivity().setSnackbar(binding.btnRegister, getString(R.string.validation_terms_conditions), true)
                return false
            }
            return true
        }
    }
}