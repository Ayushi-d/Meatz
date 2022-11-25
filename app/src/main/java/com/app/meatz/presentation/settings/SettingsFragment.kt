package com.app.meatz.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.FROM_REGISTER
import com.app.meatz.data.application.MEATZ_WHATSAPP
import com.app.meatz.data.application.PAGE_ITEM
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.openSocialLink
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentSettingsBinding
import com.app.meatz.domain.local.ProfileRsm
import com.app.meatz.domain.remote.Contacts
import com.app.meatz.domain.remote.Pages
import com.app.meatz.presentation.home.MainActivity


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel by viewModels<SettingsViewModel>()
    private val settingsRvAdapter by lazy { SettingsRvAdapter() }
    private val languageDialog by lazy { ChangeLanguageDialog(requireContext()) }
    private lateinit var settingsList: ArrayList<ProfileRsm>
    private lateinit var contacts: Contacts
    private var whatsapp = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
            getPages()
            handleSocialClicks()
        }
    }

    private fun getContacts() {
        viewModel.getContacts().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        contacts = it
                        handleSocialViews()
                        handleSocialClicks()
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.ivFacebook, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.clRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun handleSocialClicks() {
        if (this::contacts.isInitialized) {
            binding.apply {
                ivFacebook.setOnClickListener {
                    requireActivity().openSocialLink("facebook", contacts.facebook)
                }
                ivTwitter.setOnClickListener { requireActivity().openSocialLink("twitter", "https://twitter.com/Meatzkw") }
                ivInstagram.setOnClickListener { requireActivity().openSocialLink("instagram", contacts.instagram) }
            }
        }
    }

    private fun handleSocialViews() {
        if (this::contacts.isInitialized) {
            whatsapp = contacts.whatsapp
            binding.apply {
                if (contacts.facebook.isNotEmpty())
                    ivFacebook.visible()
                if (contacts.instagram.isNotEmpty())
                    ivInstagram.visible()
                if (contacts.twitter.isNotEmpty())
                    ivTwitter.visible()
            }
        }


    }

    private fun initSettingsRv(pagesList: List<Pages>) {
        settingsList = ArrayList()
        settingsList.add(ProfileRsm(R.drawable.ic_notificationsblack, getString(R.string.settings_notification)))
        settingsList.add(ProfileRsm(R.drawable.ic_change_language, getString(R.string.settings_change_language)))
        if (pagesList.isNotEmpty()) {
            pagesList.forEach {
                settingsList.add(ProfileRsm(title = it.title, isApiPage = true, apiPageImage = it.image, apiPageId = it.id))
            }
        }

        settingsList.add(ProfileRsm(R.drawable.ic_contact_us, getString(R.string.settings_contact_us)))
        settingsRvAdapter.fill(settingsList)

        binding.rvSettings.apply {
            linearLayoutManager()
            adapter = settingsRvAdapter
        }
        settingsRvAdapter.setOnClickListener { _, item, position ->
            when (position) {
                1 -> languageDialog.show()
                settingsRvAdapter.itemCount - 1 -> {
                    val bundle = Bundle()
                    bundle.putString(MEATZ_WHATSAPP, whatsapp)
                    mainController.navigate(R.id.action_settings_contactus, bundle)
                }
                else -> getPage(item.apiPageId)
            }

        }
    }

    private fun getPages() {
        viewModel.getPages().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        shimmer.root.visible()
                        clRoot.gone()
                    }
                SUCCESS -> {
                    it?.data?.let {
                        initSettingsRv(it)
                    }
                    getContacts()
                }
                FAILURE -> {
                    binding.clRoot.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    fun getPage(pageId: Int) {
        viewModel.getPage(pageId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        val bundle = Bundle()
                        bundle.putBoolean(FROM_REGISTER, false)
                        bundle.putParcelable(PAGE_ITEM, it)
                        mainController.navigate(R.id.action_settings_page, bundle)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFacebook, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFacebook, it?.message.toString(), true)
                }
            }
        })
    }

}