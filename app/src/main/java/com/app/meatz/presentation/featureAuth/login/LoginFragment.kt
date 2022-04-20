package com.app.meatz.presentation.featureAuth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
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
import com.app.meatz.databinding.FragmentLoginBinding
import com.app.meatz.domain.remote.User
import com.app.meatz.presentation.featureAuth.AuthViewModel
import com.app.meatz.presentation.home.MainActivity
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var facebookCallback: CallbackManager
    private val googleCode = 502
    private val socialHashMap: HashMap<String, Any> by lazy { HashMap() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()
        viewModel.setLoginScreenIsShown()
    }

    private fun validateInputs(): Boolean {
        binding.apply {
            if (etEmail.isEmpty()) {
                requireActivity().setSnackbar(binding.btnLogin, getString(R.string.validation_empty_email), true)
                etEmail.requestFocus()
                return false
            } else if (!etEmail.isEmailValid()) {
                requireActivity().setSnackbar(binding.btnLogin, getString(R.string.validation_invalid_email), true)
                etEmail.requestFocus()
                return false
            } else if (etPassword.isEmpty()) {
                requireActivity().setSnackbar(binding.btnLogin, getString(R.string.validation_empty_password), true)
                etPassword.requestFocus()
                return false
            }
            return true
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallback.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == googleCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                socialHashMap.clear()
                val id = account.id ?: ""
                val firstName = account.givenName ?: ""
                val lastName = account.familyName ?: ""
                val email = account.email ?: ""

                socialHashMap["social_type"] = "google"
                socialHashMap["social_id"] = id
                socialHashMap["first_name"] = firstName
                socialHashMap["last_name"] = lastName
                socialHashMap["email"] = email
                socialLogin()

            } catch (e: ApiException) {

            }
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        FacebookSdk.sdkInitialize(requireActivity().applicationContext)
        facebookCallback = CallbackManager.Factory.create()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun handleClicks() {
        binding.apply {
            btnLogin.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (validateInputs())
                    login()
            }
            tvRegister.setOnClickListener { authController.navigate(R.id.action_login_signup) }
            tvForgetPassword.setOnClickListener { authController.navigate(R.id.action_login_forgotPassword) }
            tvContiueAsGuest.setOnClickListener { startActivity(intentFor<MainActivity>().clearTop().newTask()) }
            ivGoogle.setOnClickListener { requestGoogleAuth() }
            ivFacebook.setOnClickListener {
                btnFb.apply {
                    fragment = this@LoginFragment
                    setReadPermissions(listOf("email", "public_profile"))
                    requestFbAuth()
                    performClick()
                }
            }
        }
    }


    private fun login() {
        viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString()).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message, true)
                    it?.data?.let {
                        viewLifecycleOwner.lifecycle.coroutineScope.launch {
                            delay(1000)
                            successLogin(it)
                        }
                    }

                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message, false)
                }
            }
        })
    }


    private fun requestFbAuth() {

        LoginManager.getInstance().logOut()
        LoginManager.getInstance().loginBehavior = LoginBehavior.SSO_WITH_FALLBACK
        LoginManager.getInstance()
                .registerCallback(facebookCallback, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.accessToken?.let {
                            getUserProfile(it)
                        }
                    }

                    override fun onCancel() {
                        requireActivity().setSnackbar(binding.btnLogin, getString(R.string.login_social_failure))
                    }

                    override fun onError(error: FacebookException?) {
                        requireActivity().setSnackbar(binding.btnLogin, getString(R.string.login_social_failure))
                    }

                })
    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
                currentAccessToken
        ) { `object`, _ ->
            try {
                socialHashMap.clear()
                socialHashMap["social_type"] = "facebook"
                socialHashMap["social_id"] = `object`.getString("id")
                socialHashMap["first_name"] = `object`.getString("first_name")
                socialHashMap["last_name"] = `object`.getString("last_name")
                var email = ""
                if (`object`.has("email")) {
                    email = `object`.getString("email")
                }

                socialHashMap["email"] = email
                socialLogin()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun requestGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        GoogleSignIn.getClient(requireActivity(), gso).signOut()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, googleCode)
    }

    private fun socialLogin() {
        viewModel.socialLogin(socialHashMap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {

                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message, true)
                    it?.data?.let {
                        viewLifecycleOwner.lifecycle.coroutineScope.launch {
                            delay(1000)
                            successLogin(it)
                        }
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message, false)
                }
            }
        })
    }

    private fun successLogin(user: User) {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            delay(500)
            viewModel.cacheUserData(user)
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }
    }

}