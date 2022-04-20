package com.app.meatz.data.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt


/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

/**
 * Dismiss keyboards
 * @param view View of current focus
 */
fun Context.hideKeyboard(view: View?) = view?.let {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}


fun String.requestBodyFromString(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun roundPrice(price: Int): String {
    if (price == 0)
        return "0.000"
    else {
        val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
        return df.format(price.toDouble()).toString()
    }
}

fun roundDoublePrice(price: Double): String {
    if (price.roundToInt() == 0)
        return "0.000"
    else {
        val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
        return df.format(price.toDouble()).toString()
    }
}


/**
 * Get color from resources
 * @param colorId Id of color which will load
 * @return Chosen color
 */
fun Context.getColorCompat(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

/**
 * Get drawable from resources
 * @param drawableId Id of drawable which will load
 * @return Chosen drawable
 */
fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

fun Activity.transparentStatusbar(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w: Window = window
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        getWindow().navigationBarColor = Color.BLACK
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getWindow().statusBarColor = Color.BLACK
    }


}




@Suppress("DEPRECATION")
fun Context.isConnectedToNetwork(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

fun Context.openSocialLink(key: String, link: String) {
    var uri = Uri.parse(link)
    val intent = try {
        when (key) {
            "facebook" -> {
                packageManager.getPackageInfo("com.facebook.katana", PackageManager.GET_ACTIVITIES)
                Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/${uri.lastPathSegment}"))
            }
            "instagram" -> {
                packageManager.getPackageInfo("com.instagram.android", PackageManager.GET_ACTIVITIES)
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/${uri.lastPathSegment}"))
            }

            "whatsapp" -> {
                uri = Uri.parse("https://wa.me/:$link")
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                Intent(Intent.ACTION_VIEW, uri)
            }
            else -> Intent(Intent.ACTION_VIEW, uri)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Intent(Intent.ACTION_VIEW, uri)
    }
    startActivity(intent)
}





