package com.app.meatz.data.utils.extensions

import android.util.Patterns
import android.widget.EditText


fun EditText.isEmpty(): Boolean {
    return this.text.toString().trim().isEmpty()
}

fun EditText.isEmailValid() = Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()

fun EditText.isPhoneValid(): Boolean {
    return this.text.toString().trim().length == 8
}

fun EditText.isPasswordValid(): Boolean {
    return this.text.toString().trim().length >= 8
}