package com.app.meatz.presentation.cart.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.extensions.isPhoneValid
import com.app.meatz.data.utils.extensions.onChange
import com.app.meatz.databinding.DialogPhoneNumberBinding

class AddPhoneNumberDialog(context: Context) : AppCompatDialog(context) {
    private val binding = DialogPhoneNumberBinding.inflate(layoutInflater)
    private var phoneNumber = ""

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            etPhone.onChange {
                etPhone.error = null
            }
        }
    }

    fun clickOnAddPhoneBtn(action: () -> Unit = {}) {
        binding.apply {
            btnAdd.setOnClickListener {
                if (etPhone.isEmpty()) {
                    etPhone.requestFocus()
                    etPhone.error = context.getString(R.string.validation_empty_phone)
                } else if (!etPhone.isPhoneValid()) {
                    etPhone.requestFocus()
                    etPhone.error = context.getString(R.string.validation_invalid_phone)
                } else {
                    context.hideKeyboard(it)
                    phoneNumber = etPhone.text?.trim().toString()
                    action.invoke()
                    dismiss()
                }
            }
        }

    }

    fun getPhoneNumber(): String = phoneNumber
}