package com.app.meatz.presentation.ads

import android.os.Bundle
import androidx.activity.viewModels
import com.app.meatz.core.BaseActivity
import com.app.meatz.data.application.ADS_URL
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.transparentStatusbar
import com.app.meatz.databinding.ActivityAdsBinding
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.languageSelection.LanguageSelectionViewModel
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class AdsActivity : BaseActivity<ActivityAdsBinding>() {
    private var adsUrl = ""
    private val viewModel by viewModels<LanguageSelectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.transparentStatusbar(window)
        intent?.let {
            adsUrl = it.getStringExtra(ADS_URL) ?: ""
        }
        if (!viewModel.loginScreenIsShown() && adsUrl.isEmpty()){
            startActivity(intentFor<AuthActivity>().clearTask().newTask())
        }else if (viewModel.loginScreenIsShown() && adsUrl.isEmpty()){
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }
        binding.ivAds.loadWithPlaceHolder(adsUrl)
        handleViewsClicks()
    }

    private fun handleViewsClicks() {

        binding.apply {
            btnSkip.setOnClickListener {
                if (!viewModel.loginScreenIsShown())
                    startActivity(intentFor<AuthActivity>().clearTask().newTask())
                else
                    startActivity(intentFor<MainActivity>().clearTask().newTask())
            }
        }
    }

}