package com.app.meatz.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.app.meatz.R
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ActivityBaseBinding
import com.app.meatz.presentation.dialogs.AlertDialog
import com.app.meatz.presentation.dialogs.LoadingDialog
import com.app.meatz.presentation.home.MainActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.lang.reflect.ParameterizedType

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

  protected val binding by lazy { initBinding() }
  private val baseBinding by lazy { ActivityBaseBinding.inflate(layoutInflater) }
  private val loadingDialog by lazy { LoadingDialog(this) }
  private val alertDialog by lazy { AlertDialog(this) }

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(LocaleHelper.onAttach(newBase))
  }

  @Suppress("DEPRECATION")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Set the status bar to dark-semi-transparent
    setContent()
  }

  @Suppress("UNCHECKED_CAST")
  private fun initBinding(): VB {
    val superclass = javaClass.genericSuperclass as ParameterizedType
    val method = (superclass.actualTypeArguments[0] as Class<Any>)
        .getDeclaredMethod("inflate", LayoutInflater::class.java)
    return method.invoke(null, layoutInflater) as VB
  }

  private fun setContent() {
    baseBinding.flContainer.addView(binding.root)
    setContentView(baseBinding.root)
  }

  override fun onPause() {
    hideKeyboard(currentFocus)
    super.onPause()
  }

  /**
   * Show loading dialog
   */
  fun showLoading() = loadingDialog.show()

  /**
   * Dismiss loading dialog
   */
  fun dismissLoading() {
    // Make sure that activity is alive otherwise IllegalArgumentException will arise.
    if (isDestroyed.not()) loadingDialog.dismiss()
  }

  /**
   * Show inner loading view
   */
  protected fun showInnerLoading() = with(baseBinding.loadingView) { rlLoading.visible() }

  /**
   * Dismiss inner loading view
   */
  protected fun dismissInnerLoading() = with(baseBinding.loadingView) { rlLoading.gone() }

  /**
   * Show error view
   * @param drawable Image that represent status (Default is connection image)
   * @param message Status message
   * @param showRetry Show or hide retry button (Default is true)
   * @param action Retry button text (Default is Retry)
   * @param onRetry Retry button on click listener
   */
  protected fun showNoInternetConnection() {
    with(baseBinding.networkView) {
        btnRetry.setOnClickListener {
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            llNetwork.gone()
        }
        llNetwork.visible()
    }
  }

  fun showAlert(
          title: String,
          message: String,
          actionTitle: String = getString(R.string.base_ok),
          action: () -> Unit = {},
          setCancelable: Boolean = false,
          stanchedOnTouch: Boolean = false,

  ) {
    alertDialog.showAlert(
            title,
            message,
            actionTitle,
            action,
            setCancelable,
            stanchedOnTouch,

    )
  }
}