package com.app.meatz.presentation.splash

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.meatz.R
import com.app.meatz.data.application.ADS_URL
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.setSnackbarWithAction
import com.app.meatz.presentation.ads.AdsActivity
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.languageSelection.LanguageSelectionActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()
    private var languageIsSelected = false
    private var loginScreenIsShown = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageIsSelected = viewModel.getLanguageIsSelected()
        loginScreenIsShown = viewModel.loginScreenIsShown()
        getAds()
        if (viewModel.notificationIsAllowed())
            subscribeOnTopic()
        printHashKey(this)

    }


    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i("hashKey", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("dx", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("dx", "printHashKey()", e)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onBackPressed() {}

    private fun navigateInCaseAdsUrl(imageurl: String) {
        if (!languageIsSelected) startActivity(intentFor<LanguageSelectionActivity>().putExtra(ADS_URL, imageurl))
        else
            startActivity(intentFor<AdsActivity>().clearTask().newTask().putExtra(ADS_URL, imageurl))
    }

    private fun navigationInCaseEmptyAdsUrl() {
        if (!languageIsSelected) startActivity(intentFor<LanguageSelectionActivity>().putExtra(ADS_URL, ""))
        else {
            if (!loginScreenIsShown)
                startActivity(intentFor<AuthActivity>().clearTask().newTask())
            else
                startActivity(intentFor<MainActivity>().clearTask().newTask())
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

    private fun getAds() {
        viewModel.getAds().observe(this) {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let {
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(2000)
                            if (it.image.isNotEmpty())
                                navigateInCaseAdsUrl(it.image)
                            else
                                navigationInCaseEmptyAdsUrl()
                        }
                    }
                }
                FAILURE -> {
                    this.setSnackbarWithAction(message = it.message, legnth = BaseTransientBottomBar.LENGTH_INDEFINITE,
                            actionTitle = getString(R.string.global_tab_to_retry), action = { getAds() })

                }

            }
        }
    }


}
