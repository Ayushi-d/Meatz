package com.app.meatz.presentation.dialogs

import android.app.Activity
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window.FEATURE_NO_TITLE
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.databinding.DialogLoadingBinding

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class LoadingDialog(context: Activity) : AppCompatDialog(context, R.style.LoadingTheme) {

  private val binding = DialogLoadingBinding.inflate(layoutInflater)

  init {
    window?.apply {
      setLayout(WRAP_CONTENT, WRAP_CONTENT)
      setGravity(CENTER)

    }

    requestWindowFeature(FEATURE_NO_TITLE)
    setContentView(binding.root)
    setCancelable(false)


  }
}