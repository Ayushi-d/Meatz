package com.app.meatz.presentation.cart.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.databinding.DialogContinueGuestBinding
import com.app.meatz.presentation.featureAuth.AuthActivity

class ContinueGuestDialog(context: Context) : AppCompatDialog(context) {
    private val binding = DialogContinueGuestBinding.inflate(layoutInflater)

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView(binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        binding.apply {
            btnContinueShopping.setOnClickListener {
                dismiss()
            }
            btnSignIn.setOnClickListener {
                val intnet = Intent(context, AuthActivity::class.java)
                context.startActivity(intnet)
                dismiss()
            }
        }
    }

    fun setonCheckoutClick(action: () -> Unit = {}) {
        binding.btnContinueGuest.setOnClickListener {
            action.invoke()
            dismiss()
        }
    }
}