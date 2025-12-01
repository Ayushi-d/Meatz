package com.app.meatz.presentation.splash

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.meatz.R
import com.app.meatz.core.BaseActivity
import com.app.meatz.data.application.ADS_URL
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.setSnackbarWithAction
import com.app.meatz.databinding.ActivitySplashVideoMeatzBinding
import com.app.meatz.presentation.ads.AdsActivity
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.languageSelection.LanguageSelectionActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
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


class SplashVideoMeatz : AppCompatActivity() {

    var simpleVideoView: VideoView? = null
    private val viewModel by viewModels<SplashViewModel>()
    private var languageIsSelected = false
    private var loginScreenIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_video_meatz)
        simpleVideoView = findViewById(R.id.videoView)
        simpleVideoView!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.video))
        simpleVideoView!!.requestFocus()
        simpleVideoView!!.start()

        languageIsSelected = viewModel.getLanguageIsSelected()
        loginScreenIsShown = viewModel.loginScreenIsShown()

        if (viewModel.notificationIsAllowed())
            subscribeOnTopic()
        printHashKey(this)

        simpleVideoView!!.setOnCompletionListener {
            getAds()
        }

        // display a toast message if any
        // error occurs while playing the video
        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An Error Occurred " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }

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
        if (!languageIsSelected) startActivity(intentFor<LanguageSelectionActivity>().putExtra(
            ADS_URL, imageurl))
        else
            startActivity(intentFor<AdsActivity>().clearTask().newTask().putExtra(ADS_URL, imageurl))
    }

    private fun navigationInCaseEmptyAdsUrl() {
        if (!languageIsSelected) startActivity(intentFor<LanguageSelectionActivity>().putExtra(
            ADS_URL, ""))
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
                    if (it.data == null)
                        navigationInCaseEmptyAdsUrl()
                    else
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