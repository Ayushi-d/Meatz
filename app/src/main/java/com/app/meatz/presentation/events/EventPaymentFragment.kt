package com.app.meatz.presentation.events

import android.os.Bundle
import android.view.View
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.databinding.FragmentEventPaymentBinding

class EventPaymentFragment : BaseFragment<FragmentEventPaymentBinding>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvBackToHome.setOnClickListener{
                mainController.popBackStack(R.id.homeFragment, false)
            }
        }
    }
}