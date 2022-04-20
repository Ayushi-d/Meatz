package com.app.meatz.presentation.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentNotificationsBinding
import com.app.meatz.presentation.home.MainActivity

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {
    private val viewModel by viewModels<NotificationsViewModel>()
    private val notificationRvAdapter by lazy { NotificationRvAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNotificationRv()
        getNotifications(1)
    }

    private fun initNotificationRv() {
        binding.rvNotification.apply {
            linearLayoutManager()
            adapter = notificationRvAdapter

        }
        notificationRvAdapter.apply {
            setOnLoadMoreListener { getNotifications(it) }
            setOnClickListener { _, item, _ ->
                val bundle = Bundle()
                when (item.model) {
                    "order" -> {
                        if (item.modelId != 0) {

                            item.modelId.let { bundle.putInt(ORDER_ID, it) }
                            mainController.navigate(R.id.action_notification_orderDetails, bundle)
                        }
                    }
                }
            }
        }
    }

    private fun getNotifications(page: Int) {
        viewModel.getNotifications(page).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> if (page == 1) {
                    binding.apply {
                        shimmer.root.visible()
                        clRoot.gone()
                    }
                }
                SUCCESS -> {
                    binding.apply {
                        shimmer.root.gone()
                        clRoot.visible()
                    }
                    it?.data?.let {
                        if (page == 1) {
                            if (it.isEmpty())
                                binding.apply {
                                    emptyLayout.visible()
                                    rvNotification.gone()
                                }
                            else {
                                notificationRvAdapter.fill(it)
                            }
                        } else
                            notificationRvAdapter.addItems(it)
                    }

                    it?.pagination?.let { pagination ->
                        if (pagination.currentPage == pagination.lastPage) notificationRvAdapter.setLoaded()
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.tvEmpty, it?.message.toString(), true)
                }
                FAILURE -> {
                    if (page == 1) {
                        binding.clRoot.gone()
                        handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                    } else
                        notificationRvAdapter.showError()
                }
            }
        })
    }
}