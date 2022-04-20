package com.app.meatz.data.utils

import android.app.Activity
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.meatz.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


//defaultSnackbar
fun Activity.setSnackbar(view: View = this.window.decorView, message: String, centerTitle: Boolean = false) {
    val snackbar: Snackbar = Snackbar.make(
            view, message, BaseTransientBottomBar.LENGTH_LONG)
    setBackgroundColor(this, snackbar)
    handleMessageTextview(this, snackbar, centerTitle)
    snackbar.show()
}


//regionSnackbarWithAction
fun Activity.setSnackbarWithAction(view: View = this.window.decorView, message: String,
                                   legnth: Int = BaseTransientBottomBar.LENGTH_LONG,
                                   actionTitle: String,
                                   action: () -> Unit) {
    val snackbar: Snackbar = Snackbar.make(view, message, legnth)
    setBackgroundColor(this, snackbar)
    handleMessageTextview(this, snackbar, false)
    setAction(snackbar, actionTitle, action)
    snackbar.show()

}

fun Activity.setAction(snackbar: Snackbar, actionTitle: String, action: () -> Unit = {}) {
    val snackbarAction = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
    snackbarAction?.isAllCaps = false
    setTextviewFormat(snackbarAction)
    snackbar.setAction(actionTitle) {
        action.invoke()
    }
}

//endregion

//setFontForTextview
private fun Activity.setTextviewFormat(textView: TextView) {
    val faceEn = ResourcesCompat.getFont(this, R.font.poppins_regular)
    val faceAr = ResourcesCompat.getFont(this, R.font.almarai_regular)
    if (LocaleHelper.locale == "en")
        textView.typeface = faceEn
    else
        textView.typeface = faceAr

}


private fun handleMessageTextview(activity: Activity, snackbar: Snackbar, centerTitle: Boolean, maxLines: Int = 3) {
    val tvSnackbar = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    activity.setTextviewFormat(tvSnackbar)
    if (centerTitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tvSnackbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
        else
            tvSnackbar.gravity = Gravity.CENTER_HORIZONTAL
    }

    tvSnackbar.maxLines = maxLines
    tvSnackbar.setPadding(0, 40, 0, 40)
}


private fun setBackgroundColor(activity: Activity, snackbar: Snackbar) {
    snackbar.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.merlot))
    val params: ViewGroup.MarginLayoutParams = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    snackbar.view.layoutParams = params
    snackbar.view.background = ContextCompat.getDrawable(activity, R.drawable.bg_snackbar)
}

