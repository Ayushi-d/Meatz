package com.app.meatz.presentation.main

import android.app.Activity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.NavigationUI
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.databinding.ActivityMainBinding
import java.util.*


class NavigationArchitecture(
        var controller: NavController,
        var binding: ActivityMainBinding,
        var activity: Activity

) {
    var addToBackStack: Boolean = true
    var fragmentBackStack: Stack<Int>

    fun linkBottomMenuWithNavigationArch() {
        NavigationUI.setupWithNavController(binding.bottomNavigationView, controller)
    }

    init {

        fragmentBackStack = Stack()

    }

    fun setonDestinationListeners() {
        controller.addOnDestinationChangedListener { _, destination, _ ->
            defaultNavigation(destination)
            when (destination.id) {
                R.id.homeFragment, R.id.myOrdersFragment, R.id.myBoxesFragment -> {
                    showBottomNavigation()
                    showNotificationIconAndHideBackIcon()
                    showCartIcon()
                    showLogo()
                }
                R.id.shopDetailsFragment -> {
                    hideBottomNavigation()
                    hideNotificationIconAndShowBackIcon()
                    showCartIcon()
                    showLogo()
                }
                R.id.categoryShopsFragment, R.id.ourSelectionFragment, R.id.trendingProducts, R.id.eventDetail, R.id.addEventDetail, R.id.productDetailsFragment, R.id.offerBoxDetailsFragment -> {
                    hideBottomNavigation()
                    hideNotificationIconAndShowBackIcon()
                    showCartIcon()
                    showLogo()
                }
                R.id.ourBoxesFragment, R.id.editProfileFragment, R.id.changePasswordFragment, R.id.wishlistFragment, R.id.boxDetailsFragment, R.id.addressFragment,
                R.id.myOrdersFragment, R.id.orderDetailsFragment, R.id.newBoxFragment, R.id.myBoxDetailsFragment, R.id.contactUsFragment, R.id.pageFragment2,
                R.id.notificationsFragment, R.id.cartFragment, R.id.checkoutFragment, R.id.paymentFragment, R.id.searchFragment -> {
                    hideBottomNavigation()
                    hideNotificationIconAndShowBackIcon()
                    hideCartIcon()
                    showLogo()
                }

                R.id.myWalletFragment -> {
                    hideBottomNavigation()
                    hideNotificationIconAndShowBackIcon()
                    hideCartIcon()
                    showLogo()
                    binding.toolbar.visible()
                }
                R.id.profileFragment, R.id.settingsFragment -> {
                    showBottomNavigation()
                    hideCartIcon()
                    showNotificationIconAndHideBackIcon()
                    showLogo()
                }
                R.id.checkoutStatusFragment -> {
                    hideToolbarViews()
                    hideBottomNavigation()
                    hideLogo()

                }
            }
        }
    }

    fun onBackPressed(activity: Activity) {
        if (fragmentBackStack.size > 1) {
            //we will check if this of
            when (fragmentBackStack.lastElement()) {
                R.id.myOrdersFragment, R.id.myBoxesFragment, R.id.profileFragment, R.id.settingsFragment -> {
                    if (controller.currentDestination?.id != R.id.orderDetailsFragment) {
                        fragmentBackStack.pop()
                    }
                    when (fragmentBackStack.lastElement()) {
                        R.id.myOrdersFragment, R.id.myBoxesFragment, R.id.profileFragment, R.id.settingsFragment -> {
                            val fragmentId = fragmentBackStack.lastElement()
                            addToBackStack = false
                            controller.navigate(fragmentId)
                        }
                        else -> {
                            controller.popBackStack()
                        }
                    }

                }

                R.id.homeFragment -> {
                    activity.finish()
                }
                else -> {
                    if (fragmentBackStack.lastElement() == R.id.myOrdersFragment && controller.currentBackStackEntry?.destination?.id == R.id.checkoutStatusFragment)
                        controller.popBackStack(R.id.homeFragment, false)
                    else
                        controller.popBackStack()
                }
            }


        } else {

            if (fragmentBackStack.size == 1) {
                activity.finish()
            } else {
                activity.onBackPressed()
            }
        }
    }

    private fun defaultNavigation(destination: NavDestination) {

        if (addToBackStack) {
            if (!fragmentBackStack.contains(destination.id)) {
                fragmentBackStack.add(destination.id)
            } else if (fragmentBackStack.contains(destination.id)) {
                if (destination.id == R.id.homeFragment) {
                    val homeCount =
                            Collections.frequency(fragmentBackStack, R.id.homeFragment)
                    if (homeCount < 2) {
                        fragmentBackStack.push(destination.id)
                    } else {
                        fragmentBackStack.asReversed().remove(destination.id)
                        fragmentBackStack.push(destination.id)
                    }
                } else {
                    fragmentBackStack.remove(destination.id)
                    fragmentBackStack.push(destination.id)
                }
            }

        }
        addToBackStack = true
    }


    private fun hideBottomNavigation() {
        if (!binding.bottomNavigationView.isGone) {
            hideViewsAnimation(binding.bottomNavigationView)
        }
        binding.bottomNavigationView.gone()
    }

    private fun showBottomNavigation() {
        if (activity.isConnectedToNetwork()) {
            if (binding.bottomNavigationView.isGone) {
                showViewsAnimation(binding.bottomNavigationView)
            }
            binding.bottomNavigationView.visible()
        }

    }


    private fun hideToolbarViews() {
        binding.apply {
            ivCart.gone()
            ivBack.gone()
            cartCount.gone()
            ivNotification.gone()
        }
    }

    private fun hideLogo() {
        binding.ivLogo.gone()
    }

    private fun showLogo() {
        binding.ivLogo.visible()
    }

    private fun hideCartIcon() {
        binding.ivCart.gone()
        binding.cartCount.gone()
    }

    private fun showCartIcon() {
        binding.ivCart.visible()
        binding.cartCount.visible()
    }

    private fun hideNotificationIconAndShowBackIcon() {
        binding.ivBack.visible()
        binding.ivNotification.gone()
    }

    private fun showNotificationIconAndHideBackIcon() {
        binding.ivNotification.visible()
        binding.ivBack.gone()
    }


}