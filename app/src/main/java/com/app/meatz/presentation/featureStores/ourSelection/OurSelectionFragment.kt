package com.app.meatz.presentation.featureStores.ourSelection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.SHOP_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.GridSpacingItemDecoration
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentOurSelectionBinding
import com.app.meatz.presentation.featureStores.StoresViewModel
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.shared.StoressRvAdapter


class OurSelectionFragment : BaseFragment<FragmentOurSelectionBinding>() {
    private val shopAdapter by lazy { StoressRvAdapter() }
    private val viewModel by viewModels<StoresViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
            initOurSelectionRv()
            getOurSelection()
        }


    }


    private fun getOurSelection() {
        viewModel.getOurSelectionStores().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    clRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    it?.data?.let {
                        binding.apply {
                            clRoot.visible()
                            shimmer.root.gone()
                        }
                        shopAdapter.fill(it.storeData)
                    }
                }
                ERROR -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.gone()
                    }
                    requireActivity().setSnackbar(binding.tvTitle, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.gone()
                    }
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initOurSelectionRv() {

        val gridlayout = GridLayoutManager(activity, 3)

        binding.rvOurSelection.apply {
            layoutManager = gridlayout
            adapter = shopAdapter

        }
        if (binding.rvOurSelection.itemDecorationCount == 0)
            binding.rvOurSelection.addItemDecoration(GridSpacingItemDecoration(3, 30, false))
        shopAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(SHOP_ID, item.id)
            mainController.navigate(R.id.action_ourSelection_shopDetails, bundle)
        }

    }
}