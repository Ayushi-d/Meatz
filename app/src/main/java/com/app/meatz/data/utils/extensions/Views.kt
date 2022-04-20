package com.app.meatz.data.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.RecyclerView.VERTICAL

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

/**
 * Set view visibility visible
 */
fun View.visible() {
  visibility = VISIBLE
}

/**
 * Set view visibility invisible
 */
fun View.invisible() {
  visibility = INVISIBLE
}

/**
 * Set view visibility gone
 */
fun View.gone() {
  visibility = GONE
}

/**
 * Avoid boilerplate code when inflating layout
 * @param layout Layout resource
 * @return Inflated view
 */
fun ViewGroup.inflate(@LayoutRes layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)

/**
 * Set LinearLayoutManager to RecyclerView
 * @param orientation (VERTICAL or HORIZONTAL)
 */

fun RecyclerView.addItemDecoration(@RecyclerView.Orientation orientation: Int = VERTICAL) {
    val dividerItemDecoration = DividerItemDecoration(
            context,
            orientation
    )
    this.addItemDecoration(dividerItemDecoration)
}

fun RecyclerView.linearLayoutManager(@Orientation orientation: Int = VERTICAL) {
    layoutManager = LinearLayoutManager(this.context, orientation, false)
}

fun showViewsAnimation(view: View) {
    val animation: Animation = TranslateAnimation(-0f, -0f, 300f, 0f)
    animation.duration = 800
    animation.fillAfter = false
    view.animation = animation
}

fun hideViewsAnimation(view: View) {
    val animation: Animation = TranslateAnimation(-0f, -0f, 0f, 300f)
    animation.duration = 800
    animation.fillAfter = false
    view.animation = animation
}


fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString().trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}
//endregion