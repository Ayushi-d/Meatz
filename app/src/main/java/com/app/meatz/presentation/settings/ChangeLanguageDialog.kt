package com.app.meatz.presentation.settings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.data.preferences.notificationsAllowed
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.databinding.DialogChangeLanguageBinding
import com.app.meatz.presentation.home.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask


class ChangeLanguageDialog(context: Context) : AppCompatDialog(context) {
    private val binding = DialogChangeLanguageBinding.inflate(layoutInflater)

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            if (LocaleHelper.locale == "en") {
                rbAr.textDirection = View.TEXT_DIRECTION_RTL
                rbEn.textDirection = View.TEXT_DIRECTION_RTL
                rbEn.isChecked = true
            } else {
                rbAr.isChecked = true
                rbAr.textDirection = View.TEXT_DIRECTION_LTR
                rbEn.textDirection = View.TEXT_DIRECTION_LTR
            }


            rgLanguage.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    when (checkedId) {
                        R.id.rbEn -> LocaleHelper.locale = "en"
                        R.id.rbAr -> LocaleHelper.locale = "ar"
                    }
                    if (notificationsAllowed) {
                        if (LocaleHelper.locale == "en") {
                            FirebaseMessaging.getInstance().subscribeToTopic("Android_en")
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Android_ar")
                        } else {
                            FirebaseMessaging.getInstance().subscribeToTopic("Android_ar")
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Android_en")
                        }
                    }
                    val intent = Intent(context, MainActivity::class.java).clearTask().newTask()
                    context.startActivity(intent)
                    dismiss()

                }
            })
        }
    }
}