package com.app.meatz.presentation.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.data.utils.extensions.invisible
import com.app.meatz.databinding.DialogAlertBinding

class AlertDialog(context: Context) : AppCompatDialog(context,R.style.AlerDialogTheme) {



    private val binding = DialogAlertBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setContentView( binding.root)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }


        binding.ivClose.setOnClickListener { this.cancel() }
    }

    fun showAlert(
            title: String,
            message: String,
            actionTitle: String = this.context.getString(R.string.base_ok),
            action: () -> Unit = {},
            setCancelable: Boolean = false,
            stanchedOnTouch: Boolean = false,
            hideCloseBtn: Boolean = false

    ) {
        this.setCanceledOnTouchOutside(stanchedOnTouch)
        this.setCancelable(setCancelable)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnOk.apply {
            text = actionTitle
            setOnClickListener {
                this@AlertDialog.cancel()
                action.invoke()
            }
            if (hideCloseBtn)
                binding.ivClose.invisible()
        }
        show()
    }

}