package com.app.meatz.presentation.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.meatz.R
import com.app.meatz.core.BaseActivity
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.application.REQUEST_FLEXIBLE_UPDATE
import com.app.meatz.data.preferences.IsOrderSuccess
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ActivityMainBinding
import com.app.meatz.presentation.main.NavigationArchitecture
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URI
import kotlin.math.log

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class MainActivity : BaseActivity<ActivityMainBinding>(), InstallStateUpdatedListener {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
    }
    private val controller: NavController by lazy { navHostFragment.navController }

    private val navigationArchitectureObj by lazy {
        NavigationArchitecture(controller, binding, this)
    }


    companion object {
        val showHideViewsInCaseNoConnections: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUpdate()
        showHideViewsInCaseNoConnections.value = true
        setObservers()
        navigationArchitectureObj.linkBottomMenuWithNavigationArch()
        navigationArchitectureObj.setonDestinationListeners()
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { }
        handleViewsClicks()
        checkIntentData()
        checkdeeplink()
    }


    private fun checkdeeplink(){
        val uri : Uri? = intent?.data
        if (uri != null){
            val parameters: List<String> = uri.pathSegments
            val parms = parameters[parameters.size-1]
            val bundle = Bundle()
            bundle.putInt(PRODUCT_ID, parms.toString().toInt())
            controller.navigate(R.id.productDetailsFragment,bundle)
        }
    }

    private fun checkIntentData() {
        lifecycleScope.launch {
            delay(2000)
            intent?.extras?.let {
                if (it.containsKey(ORDER_ID)) {

                    controller.navigate(
                        R.id.action_home_orderDetails, bundleOf(
                            ORDER_ID to it.get(ORDER_ID).toString().toInt()
                        )
                    )
                }

                if (it.containsKey("type")) {
                    controller.navigate(
                        R.id.action_home_orderDetails, bundleOf(
                            ORDER_ID to it.get("id").toString().toInt()
                        )
                    )
                }
            }
        }

    }
    
    override fun onBackPressed() {
        if (controller.currentDestination?.id == R.id.checkoutStatusFragment) {
            if (IsOrderSuccess)
                controller.popBackStack(R.id.homeFragment, false)
            else
                controller.popBackStack(R.id.checkoutFragment, false)
        } else
            navigationArchitectureObj.onBackPressed(this)
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) notifyUser()
        }
        checkdeeplink()
    }

    override fun onDestroy() {
        appUpdateManager.unregisterListener(this)
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_FLEXIBLE_UPDATE -> when (resultCode) {
                RESULT_OK -> {
                    // User has accepted the request for update.
                }
                RESULT_CANCELED -> {
                    // User has canceled the update or denied for the an update.
                }
                RESULT_IN_APP_UPDATE_FAILED -> {
                    // Some error has occurred during the update.
                    // It may be some error from the user side or some other error.
                }
            }
        }
    }


    override fun onStateUpdate(installState: InstallState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) notifyUser()
    }


    private fun setObservers() {
        showHideViewsInCaseNoConnections.observe(this, Observer {
            when (it) {
                false -> {
                    binding.apply {
                        toolbar.gone()
                        bottomNavigationView.gone()


                    }
                }
                else -> {
                    binding.apply {
                        toolbar.visible()
                        bottomNavigationView.visible()
                    }
                }
            }
        })

    }


    private fun handleViewsClicks() {
        binding.apply {
            ivBack.setOnClickListener {
                if (onBackPressedDispatcher.hasEnabledCallbacks())
                    onBackPressedDispatcher.onBackPressed()
                else
                    navigationArchitectureObj.onBackPressed(this@MainActivity)
            }
            ivNotification.setOnClickListener {
                controller.navigate(R.id.notificationsFragment)
            }
            ivCart.setOnClickListener {
                controller.navigate(R.id.cartFragment)
            }

        }
    }


    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.FLEXIBLE, this, REQUEST_FLEXIBLE_UPDATE
                )
            }
        }
    }

    private fun notifyUser() {
        Snackbar
            .make(
                binding.rlContainer,
                R.string.base_restart_msg,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            )
            .setAction(R.string.base_restart) {
                appUpdateManager.completeUpdate()
                appUpdateManager.unregisterListener(this)
            }.show()
    }
}

// when change language notification recieve twice in 2 languages
// addresses
