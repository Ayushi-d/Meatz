package com.app.meatz.presentation.page

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.FROM_REGISTER
import com.app.meatz.data.application.PAGE_ITEM
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.FragmentPageBinding
import com.app.meatz.domain.remote.Page


class PageFragment : BaseFragment<FragmentPageBinding>() {
    private var fromRegister = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            fromRegister = it.getBoolean(FROM_REGISTER)
            val page = it.getParcelable<Page>(PAGE_ITEM) as Page
            binding.apply {
                if (fromRegister)
                    toolbar.visible()
                else
                    toolbar.gone()
                tvTitle.text = page.title
                tvDetails.apply {
                    text = HtmlCompat.fromHtml(page.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    movementMethod = ScrollingMovementMethod()
                }
            }

        }

        binding.ivBack.setOnClickListener {
            if (fromRegister)
                authController.popBackStack()
            else
                mainController.popBackStack()

        }

    }
}