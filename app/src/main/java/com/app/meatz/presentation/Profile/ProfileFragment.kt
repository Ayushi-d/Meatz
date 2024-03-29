package com.app.meatz.presentation.Profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.MEATZ_WHATSAPP
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.preferences.USER
import com.app.meatz.data.preferences.registerOnPrefChangeListener
import com.app.meatz.data.preferences.unregisterOnPrefChangeListener
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentProfileBinding
import com.app.meatz.domain.local.ProfileRsm
import com.app.meatz.domain.remote.Contacts
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.settings.ChangeLanguageDialog
import org.jetbrains.anko.support.v4.intentFor


class ProfileFragment : BaseFragment<FragmentProfileBinding>(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val viewModel by viewModels<ProfileViewModel>()
    private val profileRvAdapter by lazy { ProfileRvAdapter() }
    private lateinit var profileList: ArrayList<ProfileRsm>
    private val languageDialog by lazy { ChangeLanguageDialog(requireContext()) }
    private lateinit var contacts: Contacts
    private var whatsapp = ""
    val bundle = Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerOnPrefChangeListener(this)
        handleViewsClicks()
        updateViewAfterCheckUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterOnPrefChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if (p1 == USER)
            updateViewAfterCheckUser()
    }

    @SuppressLint("SetTextI18n")
    private fun updateViewAfterCheckUser() {
        if (viewModel.isUserLogged()) {

            binding.apply {
                shimmer.root.gone()
                lnRoot.visible()
                clLoggedUser.visible()
                clUnLoggedUser.gone()

            }
            initProfileRv()
            getUserProfile()
        } else {
            binding.apply {
                shimmer.root.gone()
                lnRoot.visible()
                clLoggedUser.gone()
                clUnLoggedUser.visible()
            }
        }
    }

    private fun handleViewsClicks() {
        binding.apply {
            btnLogout.setOnClickListener {
                showAlert(getString(R.string.profile_logout), getString(R.string.profile_logout_dialog_msg),
                        getString(R.string.global_yes), {
                    logout()
                }, true, true)
            }
            btnLogin.setOnClickListener { startActivity(intentFor<AuthActivity>()) }

            myOrders.setOnClickListener{ mainController.navigate(R.id.action_profile_myOrders) }

            myAddress.setOnClickListener { mainController.navigate(R.id.action_profile_myWallet) }

            myNotifications.setOnClickListener { mainController.navigate(R.id.action_profile_notifications) }

            myLang.setOnClickListener { languageDialog.show() }
        }
    }

    private fun initProfileRv() {
        profileList = ArrayList()
//        profileList.add(ProfileRsm(R.drawable.ic_wallet_black, getString(R.string.profile_my_wallet)))
//        profileList.add(ProfileRsm(R.drawable.ic_user, getString(R.string.profile_edit_profile)))
//        profileList.add(ProfileRsm(R.drawable.ic_my_orders, getString(R.string.profile_myorders)))
//        profileList.add(ProfileRsm(R.drawable.ic_wishlist_black, getString(R.string.profile_wishlist)))
//        profileList.add(ProfileRsm(R.drawable.ic_my_address, getString(R.string.profile_my_address)))
//        profileList.add(ProfileRsm(R.drawable.ic_lock, getString(R.string.profile_change_password)))
        profileList.add(ProfileRsm(R.drawable.ic_my_address, getString(R.string.myaddress)))
        profileList.add(ProfileRsm(R.drawable.ic_user, getString(R.string.profile_edit_profile)))
        profileList.add(ProfileRsm(R.drawable.ic_wishlist_black, getString(R.string.profile_wishlist)))
        profileList.add(ProfileRsm(R.drawable.ic_contact_us, getString(R.string.contactus)))
        profileList.add(ProfileRsm(R.drawable.meatz_logo, getString(R.string.settings_about_meatz)))
        profileList.add(ProfileRsm(R.drawable.ic_privacy_policies, getString(R.string.settings_privacy_policy)))
        profileList.add(ProfileRsm(R.drawable.ic_terms_conditions, getString(R.string.settings_terms)))
        profileList.add(ProfileRsm(R.drawable.ic_lock, getString(R.string.profile_change_password)))
        profileList.add(ProfileRsm(R.drawable.ic_trash, getString(R.string.profile_delete_account)))

        profileRvAdapter.fill(profileList)

        binding.rvProfile.apply {
            linearLayoutManager()
            adapter = profileRvAdapter
        }
        profileRvAdapter.setOnClickListener { _, _, position ->
            when (position) {
                0 -> mainController.navigate(R.id.action_profile_address)
                1 -> mainController.navigate(R.id.action_profile_edit_profile)
                2 -> mainController.navigate(R.id.action_profile_wishlist)
                3 -> mainController.navigate(R.id.action_profile_contactus,bundle)
                4 -> mainController.navigate(R.id.action_profile_page)
                5 -> mainController.navigate(R.id.action_profile_page)
                6 -> mainController.navigate(R.id.action_profile_page)
                7 -> mainController.navigate(R.id.action_profile_change_password)
                8 ->  showAlert(getString(R.string.profile_Delete), getString(R.string.profile_delete_dialog_msg),
                    getString(R.string.global_yes), {
                       deleteAccount()
                    }, true, true)

            }
        }
    }

    private fun deleteAccount(){
        viewModel.deleteAccount().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    viewModel.removerUser()
                    startActivity(intentFor<AuthActivity>())
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.lnRoot, it.message, false)
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {
        viewModel.getProfileInfo().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        lnRoot.gone()
                        shimmer.root.visible()
                    }
                SUCCESS -> {
                    binding.apply {
                        getContacts()
                        lnRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let { user ->
                        binding.apply {
                            tvEmail.text = user.email
                            tvUserName.text = user.username
                            profileRvAdapter.setWalletValue(user.wallet)

                        }
                    }
                }
                FAILURE -> {
                    binding.lnRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun getContacts() {
        viewModel.getContacts().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    binding.apply {
                        lnRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        contacts = it
                        whatsapp = contacts.whatsapp
                        bundle.putString(MEATZ_WHATSAPP, whatsapp)
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                   // requireActivity().setSnackbar(binding.ivFacebook, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.lnRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun logout() {
        viewModel.logout().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    viewModel.removerUser()
                    startActivity(intentFor<AuthActivity>())
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.lnRoot, it.message, false)

                }
            }
        })
    }

}