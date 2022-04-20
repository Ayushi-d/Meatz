package com.app.meatz.presentation.featureBoxes.ourBoxes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.BOX_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.showViewsAnimation
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentOurBoxesBinding
import com.app.meatz.domain.remote.generalResponse.Cart
import com.app.meatz.presentation.featureBoxes.BoxesViewModel
import com.app.meatz.presentation.home.MainActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class OurBoxesFragment : BaseFragment<FragmentOurBoxesBinding>() {
    private val ourBoxesAdapter by lazy { OurBoxesRvAdapter() }
    private val viewModel by viewModels<BoxesViewModel>()
    var count = 0
    private val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOurBoxesRv()
        getOurBoxes()
    }


    @SuppressLint("SetTextI18n")
    private fun getOurBoxes() {
        viewModel.getOurBoxes().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.visible()
                    }
                    count = 0
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {

                        ourBoxesAdapter.fill(it.boxes)
                        count = it.boxes.size

                        initCartViews(it.cart)

                        binding.tvItemsNumber.text = "${count} ${getString(R.string.our_boxes_available)}"
                    }

                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.tvViewCart, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initCartViews(cart: Cart) {

        if (cart.count != 0) {
            binding.apply {
                showViewsAnimation(rlCart)
                rlCart.visible()
                tvCartCount.text = cart.count.toString()
                tvCartPrice.text = getString(R.string.global_currency, df.format(cart.total).toString())
                tvViewCart.setOnClickListener {
                    mainController.navigate(R.id.action_ourBoxes_cart)
                }
            }
        }
    }

    private fun initOurBoxesRv() {
        binding.rvOurboxes.apply {
            linearLayoutManager()
            adapter = ourBoxesAdapter
        }


        ourBoxesAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(BOX_ID, item.id)
            mainController.navigate(R.id.action_ourBoxes_boxDetails, bundle)
        }
    }


}