package com.app.meatz.presentation.languageSelection

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.meatz.R
import com.app.meatz.core.BaseActivity
import com.app.meatz.data.application.ADS_URL
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.transparentStatusbar
import com.app.meatz.databinding.ActivityLanguageSelectionBinding
import com.app.meatz.presentation.ads.AdsActivity
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.home.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class LanguageSelectionActivity : BaseActivity<ActivityLanguageSelectionBinding>() {
    private var adsUrl = ""
    private val viewModel by viewModels<LanguageSelectionViewModel>()
    private var loginScreenIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.transparentStatusbar(window)
        loginScreenIsShown = viewModel.loginScreenIsShown()
        intent?.let {
            adsUrl = it.getStringExtra(ADS_URL) ?: ""
        }
        binding.apply {
            btnArabic.setOnClickListener {
                viewModel.changeLocale("ar")
                if (viewModel.notificationIsAllowed())
                    subscribeOnTopic()
                viewModel.setLanguageIsSelected()
                navigateToAds()
            }
            btnEnglish.setOnClickListener {
                viewModel.changeLocale("en")
                if (viewModel.notificationIsAllowed())
                    subscribeOnTopic()
                viewModel.setLanguageIsSelected()
                navigateToAds()
            }
        }

    }

    private fun subscribeOnTopic() {
        if (LocaleHelper.locale == "en") {
            FirebaseMessaging.getInstance().subscribeToTopic("Android_en")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Android_ar")
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("Android_ar")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Android_en")
        }
    }

    fun navigateToAds() {
        if (adsUrl.isNotEmpty())
            startActivity(intentFor<AdsActivity>().clearTask().newTask().putExtra(ADS_URL, adsUrl))
        else {

            if (!loginScreenIsShown)
                startActivity(intentFor<AuthActivity>().clearTask().newTask())
            else
                startActivity(intentFor<MainActivity>().clearTask().newTask())
        }
    }
}