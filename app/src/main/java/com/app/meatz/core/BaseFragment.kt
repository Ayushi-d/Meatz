package com.app.meatz.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.isConnectedToNetwork
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.FragmentBaseBinding
import com.app.meatz.presentation.dialogs.AlertDialog
import com.app.meatz.presentation.dialogs.LoadingDialog
import com.app.meatz.presentation.home.MainActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import java.lang.reflect.ParameterizedType

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var reload = true
    protected val binding by lazy { initBinding() }
    val mainController by lazy {
        Navigation.findNavController(this.requireActivity(), R.id.nav_host_fragment_main)
    }
    val authController by lazy { Navigation.findNavController(this.requireActivity(), R.id.nav_host_fragment_auth) }
    private val baseBinding by lazy { FragmentBaseBinding.inflate(layoutInflater) }
    private val loadingDialog by lazy { activity?.let { LoadingDialog(it) } }
    private val alertDialog by lazy { AlertDialog(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        baseBinding.flContainer.addView(binding.root)
        return baseBinding.root
    }


    override fun onDestroy() {
        // Dismiss dialog before fragment destroyed otherwise IllegalArgumentException will arise.
        dismissLoading()
        super.onDestroy()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (view != null) {
            val parentViewGroup = binding.root.parent as ViewGroup?
            parentViewGroup?.removeAllViews()
        }
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        reload = false
    }

    override fun onPause() {
        super.onPause()
        baseBinding.flContainer.visible()
        baseBinding.errorView.llError.gone()
    }

    /**
     * Show loading dialog
     */
    fun showLoading() {
        loadingDialog?.show()
    }


    /**
     * Dismiss loading dialog
     */
    fun dismissLoading() {
        // Make sure that fragment is alive otherwise IllegalArgumentException will arise.
        if (isDetached.not()) loadingDialog?.dismiss()
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
    protected fun showNoInternetConnection(
    ) {


        with(baseBinding.networkView) {
            btnRetry.setOnClickListener {
                if (requireContext().isConnectedToNetwork()) {
                    startActivity(intentFor<MainActivity>().clearTask().newTask())
                    llNetwork.gone()
                }
            }
            llNetwork.visible()
        }
    }

    protected fun handleError(message: String, action: () -> Unit = {}) {
        if (!requireActivity().isConnectedToNetwork() || message == getString(R.string.network_check_connection)) {
            action.invoke()
            showNoInternetConnection()
        } else {
            baseBinding.flContainer.gone()
            baseBinding.errorView.llError.visible()
            baseBinding.errorView.tvErrormsg.text = message

        }
    }

    fun showAlert(
            title: String,
            message: String,
            actionTitle: String = getString(R.string.base_ok),
            action: () -> Unit = {},
            setCancelable: Boolean = false,
            stanchedOnTouch: Boolean = false,
            hideCloseBtn: Boolean = false

    ) {
        alertDialog.showAlert(
                title,
                message,
                actionTitle,
                action,
                setCancelable,
                stanchedOnTouch,
                hideCloseBtn
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>)
                .getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    fun revokeRecalling(function: () -> (Unit)) {
        if (reload) {
            function.invoke()

        }

    }
}