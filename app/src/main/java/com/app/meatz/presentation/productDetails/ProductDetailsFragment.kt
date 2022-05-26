package com.app.meatz.presentation.productDetails

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.roundDoublePrice
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentProductDetailsBinding
import com.app.meatz.domain.remote.generalResponse.Option
import com.app.meatz.domain.remote.productDetails.Image
import com.app.meatz.domain.remote.productDetails.ProductDetails
import com.app.meatz.presentation.dialogs.AlertDialog
import com.app.meatz.presentation.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt


class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>() {

    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val sliderAdapter by lazy { ProductSliderVpAdapter() }
    private val optionsAdapter by lazy { OptionsRvAdapter() }
    private val countLiveData: MutableLiveData<Int> by lazy { MutableLiveData() }
    private val priceLiveData: MutableLiveData<Double> by lazy { MutableLiveData() }
    private var productId = 0
    private var prodcutPrice = 0.0
    private var maxQuantity = 0
    private var optionsPrice = 0.0
    private val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
    private val myBoxesDialog by lazy { MyBoxesDialog(requireContext()) }
    private val selectedOptions: ArrayList<Int> by lazy { ArrayList() }
    private var selectedBoxesId = ""
    private val boxUnAvailableDialog by lazy { AlertDialog(requireContext()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            productId = it.getInt(PRODUCT_ID)
        }
        getProductDetails()
        handleViewClicks()
        setObservers()
    }

    private fun getProductDetails() {
        viewModel.getProductDetails(productId).observe(viewLifecycleOwner, Observer {
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
                        initProductView(it)
                        initOptionsRv(it.options)
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.clRoot, it.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun initSliderVp(sliderList: List<Image>) {
        if (sliderList.isNotEmpty()) {
            sliderAdapter.fill(sliderList)
            if (sliderList.size == 1)
                binding.tabProductSlider.gone()

            binding.vpBoxSlider.apply {
                adapter = sliderAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
            TabLayoutMediator(binding.tabProductSlider, binding.vpBoxSlider) { _, _ -> }.attach()
        } else
            binding.flProductSlider.gone()

    }

    private fun initProductView(product: ProductDetails) {
        prodcutPrice = product.price.toDouble()
        maxQuantity = product.num
        if (maxQuantity == 0)
            handleViewInCaseItemUnavailbe()
        countLiveData.value = 1
        priceLiveData.value = countLiveData.value?.times(prodcutPrice)
        binding.apply {
            tvProductName.text = product.name
            tvProductDescription.text = product.content
            tvProductPrice.text = getString(R.string.global_currency, product.price)
            tvProductTotalPrice.text = getString(R.string.global_currency, product.price)
            if (product.priceBefore.roundToInt() != 0) {
                tvProductOldPrice.apply {
                    text = roundDoublePrice(product.priceBefore)
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
            if (product.liked == 1)
                ivFav.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_wishlist_red))
        }
    }

    private fun initOptionsRv(options: List<Option>) {
        if (options.isNotEmpty()) {
            optionsAdapter.fill(options)
            binding.apply {
                cvOption.visible()
                rvOptions.apply {
                    linearLayoutManager()
                    adapter = optionsAdapter
                }
            }

            optionsAdapter.setOnClickListener { itemview, item, _ ->
                if (itemview.id == R.id.cbOption) {

                    if (!item.isChecked) {
                        optionsPrice += item.price.toDouble()
                        val totaloptionPrice = countLiveData.value?.times(item.price.toDouble())
                        priceLiveData.value = totaloptionPrice?.let { priceLiveData.value?.plus(it) }
                        selectedOptions.add(item.id)
                        item.isChecked = true
                    } else {
                        optionsPrice -= item.price.toDouble()
                        val totaloptionPrice = countLiveData.value?.times(item.price.toDouble())
                        priceLiveData.value = totaloptionPrice?.let { priceLiveData.value?.minus(it) }
                        selectedOptions.remove(item.id)
                        item.isChecked = false
                    }

                }

            }
        }
    }

    private fun handleViewInCaseItemUnavailbe() {
        boxUnAvailableDialog.showAlert(getString(R.string.global_unavailable), getString(R.string.box_details_item_unavailable), getString(R.string.base_ok),
                setCancelable = true, stanchedOnTouch = true)
        binding.apply {
            lnPrice.gone()
            lnAddcart.gone()
            tvAddMyBox.gone()
        }
    }

    private fun setObservers() {
        countLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvCount.text = it.toString()
        })
        priceLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvProductTotalPrice.text = getString(R.string.global_currency, df.format(it).toString())
        })
    }

    private fun handleViewClicks() {
        binding.apply {
            ivFav.setOnClickListener {
                if (isUser())
                    favUnFav()
                else
                    requireActivity().setSnackbar(binding.flProductSlider, getString(R.string.product_details_login_first), true)
            }
            tvMinus.setOnClickListener { handleMinusClick(binding.tvCount.text.toString().toInt()) }
            tvPlus.setOnClickListener { handlePlusClick(binding.tvCount.text.toString().toInt()) }
            tvAddToCart.setOnClickListener {
                addToCart()
            }
            tvAddMyBox.setOnClickListener {
                if (isUser())
                    getMyBoxes()
                else
                    requireActivity().setSnackbar(binding.flProductSlider, getString(R.string.product_details_login_first), true)
            }
        }
    }

    private fun addToCart() {
        val hashmap by lazy { HashMap<String, Any>() }
        hashmap["product_id"] = productId
        hashmap["count"] = countLiveData.value.toString()
        hashmap["options"] = selectedOptions.joinToString { it -> it.toString().trim() }
        viewModel.addProductToCart(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message, true)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message)
                }
            }
        })
    }


    private fun handleMinusClick(count: Int) {
        if (count != 1) {
            countLiveData.value = countLiveData.value?.minus(1)
            priceLiveData.value = priceLiveData.value?.minus((prodcutPrice + optionsPrice))
        }
    }

    private fun handlePlusClick(count: Int) {
        if (count < maxQuantity) {
            countLiveData.value = countLiveData.value?.plus(1)
            priceLiveData.value = priceLiveData.value?.plus((prodcutPrice + optionsPrice))
        } else
            requireActivity().setSnackbar(binding.flProductSlider, getString(R.string.box_details_max_quantity), true)
    }

    private fun favUnFav() {
        viewModel.favUnFav(productId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    val isLike = it?.data?.liked
                    requireActivity().setSnackbar(binding.tvAddMyBox, it.message, true)
                    if (isLike == 0)
                        binding.ivFav.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_wishlist_path))
                    else
                        binding.ivFav.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_wishlist_red))
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.tvAddMyBox, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.flProductSlider, it.message)
                }
            }
        })
    }

    private fun getMyBoxes() {
        viewModel.getMyBox().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        if (it.isEmpty()) {
                            showAlert(getString(R.string.product_details_add_my_box),
                                    getString(R.string.product_details_add_box_msg),
                                    getString(R.string.product_details_add_box), {
                                mainController.navigate(R.id.action_productDetails_newBox)
                            }, true, true)
                        } else {
                            myBoxesDialog.showDialog(it) {
                                selectedBoxesId = myBoxesDialog.getSelectedBox().joinToString { item -> item.toString() }
                                addItemToBoxes()
                            }
                        }
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.tvAddMyBox, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message)
                }
            }
        })
    }

    private fun addItemToBoxes() {
        val hashmap by lazy { HashMap<String, Any>() }
        hashmap["product_id"] = productId
        hashmap["count"] = countLiveData.value.toString()
        hashmap["options"] = selectedOptions.joinToString(",") { it -> it.toString().trim() }.trim()
        hashmap["boxes"] = selectedBoxesId
        Log.i("selectedOptions", hashmap.entries.toString())
        viewModel.addItemToBoxes(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    showLoading()
                }
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message, true)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.ivFav, it.message)
                }
            }
        })
    }


}