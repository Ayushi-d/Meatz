package com.app.meatz.presentation.featureBoxes.boxDetails

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.BOX_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentBoxDetailsBinding
import com.app.meatz.domain.remote.BoxDetails
import com.app.meatz.domain.remote.Image
import com.app.meatz.presentation.dialogs.AlertDialog
import com.app.meatz.presentation.featureBoxes.BoxesViewModel
import com.app.meatz.presentation.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class BoxDetailsFragment : BaseFragment<FragmentBoxDetailsBinding>() {
    private val sliderAdapter by lazy { BoxSliderVpAdapter() }
    private val countLiveData: MutableLiveData<Int> by lazy { MutableLiveData() }
    private val priceLiveData: MutableLiveData<Double> by lazy { MutableLiveData() }
    private val boxUnAvailableDialog by lazy { AlertDialog(requireContext()) }
    private val viewModel by viewModels<BoxesViewModel>()
    private var boxId = 0
    private var boxPrice = 0.0
    private var maxQuantity = 0
    private val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            boxId = it.getInt(BOX_ID)
        }
        getBoxDetails()

        setObservers()
        handleClicksViews()

    }

    private fun getBoxDetails() {
        viewModel.getBoxDetails(boxId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    clRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        initSliderVp(it.images)
                        initBoxViews(it)

                    }
                }
                ERROR -> {
                  binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.tvBoxTotalPrice, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()

                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initSliderVp(sliderList: List<Image>) {
        sliderAdapter.fill(sliderList)
        if (sliderList.size == 1)
            binding.tabBoxSlider.gone()

        binding.vpBoxSlider.apply {
            adapter = sliderAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        TabLayoutMediator(binding.tabBoxSlider, binding.vpBoxSlider) { _, _ -> }.attach()
    }

    private fun initBoxViews(boxDetails: BoxDetails) {
        boxPrice = boxDetails.price.toDouble()
        maxQuantity = boxDetails.num
        countLiveData.value = 1
        priceLiveData.value = countLiveData.value?.times(boxPrice)
        if (maxQuantity == 0)
            handleViewInCaseItemUnavailbe()
        binding.apply {
            tvBoxName.text = boxDetails.name
            tvBoxDescription.text = boxDetails.content
            tvBoxPrice.text = getString(R.string.global_currency, boxDetails.price)
            tvBoxTotalPrice.text = getString(R.string.global_currency, boxDetails.price)
            tvItemsNumber.text = getString(R.string.home_persons, boxDetails.persons.toString())
            if (boxDetails.priceBefore.roundToInt() != 0) {
                tvBoxOldPrice.apply {
                    text = roundDoublePrice(boxDetails.priceBefore)
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }

    private fun setObservers() {
        countLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvCount.text = it.toString()
        })
        priceLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvBoxTotalPrice.text = getString(R.string.global_currency, df.format(it).toString())
        })

    }

    private fun handleClicksViews() {

        binding.apply {
            tvMinus.setOnClickListener { handleMinusClick(binding.tvCount.text.toString().toInt()) }
            tvPlus.setOnClickListener { handlePlusClick(binding.tvCount.text.toString().toInt()) }
            tvAddToCart.setOnClickListener {
                addToCart()
            }
        }
    }

    private fun handleViewInCaseItemUnavailbe() {
        boxUnAvailableDialog.showAlert(getString(R.string.global_unavailable), getString(R.string.box_details_item_unavailable), getString(R.string.base_ok),
                setCancelable = true, stanchedOnTouch = true)
        binding.apply {
            lnPrice.gone()
            lnAddcart.gone()
        }
    }

    private fun addToCart() {
        val hashmap by lazy { HashMap<String, Any>() }
        hashmap["product_id"] = boxId
        hashmap["count"] = countLiveData.value.toString()
        viewModel.addBoxToCart(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.flBoxSlider, it.message, true)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.flBoxSlider, it.message, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.flBoxSlider, it.message, false)

                }
            }
        })
    }

    private fun handlePlusClick(count: Int) {

        //we will check on maxCount
        if (count < maxQuantity) {
            countLiveData.value = countLiveData.value?.plus(1)
            priceLiveData.value = countLiveData.value?.times(boxPrice)
        } else
            requireActivity().setSnackbar(binding.flBoxSlider, getString(R.string.box_details_max_quantity), true)


    }



    private fun handleMinusClick(count: Int) {
        if (count != 1) {
            countLiveData.value = countLiveData.value?.minus(1)
            priceLiveData.value = countLiveData.value?.times(boxPrice)
        }
    }

}