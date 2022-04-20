package com.app.meatz.presentation.featureAuth

import android.content.Intent
import android.os.Bundle
import com.app.meatz.core.BaseActivity
import com.app.meatz.data.utils.extensions.transparentStatusbar
import com.app.meatz.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.transparentStatusbar(window)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}