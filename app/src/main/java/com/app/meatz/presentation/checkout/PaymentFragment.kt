package com.app.meatz.presentation.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.CHECKOUT_STATUS_OBJ
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.application.PAYMENT_URL
import com.app.meatz.databinding.FragmentPaymentBinding
import com.app.meatz.domain.local.CheckoutStatusRsm
import com.app.meatz.domain.remote.PaymentInfo.PaymentInfo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {
    var paymentUrl = ""
    var orderId = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            paymentUrl = it.getString(PAYMENT_URL) ?: ""
            orderId = it.getInt(ORDER_ID)
        }
        loadPaymentUrl(paymentUrl)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPaymentUrl(paymentUrl: String) {
        showLoading()
        binding.webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    Log.i("url", url)
                    if (url.contains("payment_fail")) {
                        lifecycleScope.launch {
                            val bundle = Bundle()
                            val statusObj = CheckoutStatusRsm(0, false, isPaymentSuccess = false, transactionId = "", paymentId = "")
                            bundle.putParcelable(CHECKOUT_STATUS_OBJ, statusObj)
                            mainController.navigate(R.id.action_payment_checkoutStatus, bundle)
                        }
                        return true
                    } else if (url.contains("payment_success") || url.contains("success")) {
                        lifecycleScope.launch(Dispatchers.Default) {
                            try {

                                val conn: URLConnection = URL(url).openConnection()
                                conn.connect()
                                val stream: InputStream = conn.getInputStream()
                                val json: String = convertStreamToString(stream).toString()
                                val gson = Gson()
                                Log.i("jsooon", json.toString())
                                val payment: PaymentInfo = gson.fromJson(json, PaymentInfo::class.java)
                                Log.i("paymentObj", payment.toString())
                                val checkoutStatusRsm = CheckoutStatusRsm(orderId, isCashedPayment = false, isPaymentSuccess = true, transactionId = payment.data.transactionId, paymentId = payment.data.paymentId, paymentAmount = payment.data.amount)
                                val bundle = Bundle()
                                bundle.putParcelable(CHECKOUT_STATUS_OBJ, checkoutStatusRsm)
                                lifecycleScope.launch(Dispatchers.Main) { mainController.navigate(R.id.action_payment_checkoutStatus, bundle) }

                            } catch (e: IOException) {
                                e.printStackTrace()
                                Log.d("xXx", e.toString())
                            }
                        }
                        return true
                    } else {
                        Log.d("xXx", url)
                        view?.loadUrl(url)
                        return false // return true if you want to block redirection, false otherwise
                    }

                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dismissLoading()
                }
            }
            binding.webview.loadUrl(paymentUrl)
        }
    }

    private fun convertStreamToString(`is`: InputStream): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }


}