package com.app.meatz.presentation.exception

import android.view.View
import com.app.meatz.core.BaseActivity
import com.app.meatz.databinding.ActivityExceptionBinding
import com.app.meatz.presentation.splash.SplashActivity
import org.jetbrains.anko.intentFor

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class ExceptionActivity : BaseActivity<ActivityExceptionBinding>() {

  @Suppress("UNUSED_PARAMETER")
  fun onClick(view: View) {
    startActivity(intentFor<SplashActivity>())
    finishAffinity()
  }
}