package com.app.meatz.presentation.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.AdapterView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.*
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.utils.GridSpacingItemDecoration
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentCheckoutNewBinding
import com.app.meatz.domain.local.CheckoutStatusRsm
import com.app.meatz.domain.local.DeliveryTypeRsm
import com.app.meatz.domain.local.GuestAddress
import com.app.meatz.domain.remote.CheckoutDetails
import com.app.meatz.domain.remote.address.Address
import com.app.meatz.presentation.checkout.adapter.DatesRvAdapter
import com.app.meatz.presentation.checkout.adapter.DeliveryTypeRvAdapter
import com.app.meatz.presentation.checkout.adapter.PaymentMethodRvAdapter
import com.app.meatz.presentation.checkout.adapter.TimePeriodsRvAdapter
import com.app.meatz.presentation.checkout.spinner.AddressSpAdapter
import com.app.meatz.presentation.home.MainActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt


class CheckoutFragment : BaseFragment<FragmentCheckoutNewBinding>() {

    private val viewModel by viewModels<CheckoutViewModel>()
    private var selectedPayment = "knet"
    private var couponId = ""
    private var addressId = ""
    private var orderSelectedDate = ""
    private var deliveryType = ""
    private var timePeriodsId = 0
    private val df = DecimalFormat("#.000", DecimalFormatSymbols(Locale.US))
    private lateinit var addressList: List<Address>
    private var guestAddress: GuestAddress? = null
    private var reorderOrderId = ""
    private var delivery = "0.000"
    private var discount = "0.000"
    private var subTotal = ""
    private var total = 0.000
    private var walletValue = 0.000
    private lateinit var refreshPrices: MutableLiveData<Boolean>
    private var reorder = false
    private var couponCode = ""


    private lateinit var paymentMethodRvAdapter: PaymentMethodRvAdapter
    private lateinit var deliveryRvAdapter: DeliveryTypeRvAdapter
    private lateinit var datesRvAdapter: DatesRvAdapter
    private lateinit var timePeriodsRvAdapter: TimePeriodsRvAdapter
    private var isOrderFromMultipleVendor: Boolean = false
    private val deliveryTypeList by lazy { ArrayList<DeliveryTypeRsm>() }
    private var deliveryTypeIsSelected = false
    private var isExpressDeliverySelected = false


    private lateinit var checkoutDetailsRsm: CheckoutDetails
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshPrices = MutableLiveData()
        clearSelectedValues()
        arguments?.let {
            guestAddress = it.getParcelable(ADDRESS_OBJ)
            reorderOrderId = it.getString(ORDER_ID) ?: ""
            subTotal = it.getString(SUBTOTAL) ?: ""
            total = subTotal.toDouble()
            setPricesViews()
        }
        getCheckoutDetails()
        initPaymentMethodRv()
        initDeliveryTypesRv()
        initDatesRv()
        initTimePeriodsRv()
        if (isUser())
            getAddress()
        else
            initGuestAddressView()
        handleViewsListeners()
        setObservers()


    }

    private fun clearSelectedValues() {
        selectedPayment = "knet"
        paymentMethodRvAdapter = PaymentMethodRvAdapter()

        deliveryTypeIsSelected = false
        deliveryType = ""
        delivery = "0.000"
        deliveryRvAdapter = DeliveryTypeRvAdapter()

        datesRvAdapter = DatesRvAdapter()
        timePeriodsRvAdapter = TimePeriodsRvAdapter()

        if (addressId.isNotEmpty())
            setSelectedAddressView(addressId.toInt())



        couponId = ""
        couponCode = ""

        discount = "0.000"
        orderSelectedDate = ""
        timePeriodsId = 0
        refreshPrices.value = true
        binding.apply {
            etCoupon.setText("")
            tvSuccessPromo.gone()
            tvFailPromo.gone()
            cvDateTime.gone()
            cvExpressDelivery.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshPrices.value = true
        binding.etCoupon.setText("")
    }

    @SuppressLint("SetTextI18n")
    private fun initGuestAddressView() {
        guestAddress?.let {
            binding.apply {
                tvAddAddress.gone()
                ivDownArrow.gone()
                rlNoAddress.gone()
                flselectedAddress.visible()
                rlmyAddress.visible()
                tvAddress.text = it.addressName
                tvAddressName.text = "${it.customerName}\n${it.customerEmail}\n${it.customerPhone}"
                tvAddressDescription.apply {
                    text = it.addressSummary
                    ellipsize = TextUtils.TruncateAt.END
                    isSingleLine = true
                }
            }
            refreshPrices.value = true
            setDeliveryTypeList(it.deliveryValue.toDouble(), it.expressDeliveryValue.toDouble())
        }
    }


    //regionUserAddress
    private fun getAddress() {
        viewModel.getAddress().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    binding.apply {
                        clRoot.gone()
                        shimmer.root.visible()
                    }
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        addressList = it
                        if (addressList.isEmpty()) {
                            binding.apply {
                                flselectedAddress.invisible()
                                rlNoAddress.visible()
                            }
                        } else {
                            binding.apply {
                                flselectedAddress.visible()
                            }
                            setAddressSpinner(addressList as ArrayList<Address>)
                        }
                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.btnCouponApplied, it.message)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun setAddressSpinner(addressList: ArrayList<Address>) {
        addressList.add(Address(addressList[0].address, "", addressList[0].area, 0))
        val dataAdapter = AddressSpAdapter(addressList, requireContext())
        binding.spAddress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != addressList.size - 1) {
                    addressId = p3.toString()
                    binding.tvAddress.text = dataAdapter.getSelectedAddress()
                    binding.apply {
                        rlNoAddress.gone()
                        rlmyAddress.visible()
                    }

                    setSelectedAddressView(p3.toInt())
                }
            }
        }
        binding.spAddress.adapter = dataAdapter
        binding.spAddress.setSelection(addressList.size - 1)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setSelectedAddressView(addressId: Int) {
        addressList.forEach {
            if (addressId == it.id) {

                val addressSummary = StringBuilder()
                addressSummary.append("${it.area.name} ${getString(R.string.myaddress_block)} ${it.address.block} ${getString(R.string.myaddress_street)} ${it.address.street} \n")
                addressSummary.append("${getString(R.string.myaddress_building)} ${it.address.house_number}")
                if (it.address.levelNo.isNotEmpty())
                    addressSummary.append(" ${getString(R.string.myaddress_floor)} ${it.address.levelNo}")
                binding.apply {
                    tvAddressName.text = it.addressName
                    tvAddressDescription.text = addressSummary
                    setDeliveryTypeList(it.area.delivery, it.area.delivery_express)
                }
            }
        }
    }


    private fun setDeliveryTypeList(normalPrice: Double, expressPrice: Double) {
        deliveryTypeList.clear()
        deliveryTypeList.add(DeliveryTypeRsm(getStringByLocal(R.string.checkout_Express), roundDoublePrice(expressPrice)))
        deliveryTypeList.add(DeliveryTypeRsm(getStringByLocal(R.string.checkout_normal), roundDoublePrice(normalPrice)))
        deliveryRvAdapter.fill(deliveryTypeList)
    }

    //endregion
    //regionCheckout

    private fun validateCheckout(): Boolean {

        if (isUser()) {
            if (this::addressList.isInitialized) {

                if (addressList.isEmpty()) {
                    requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_empty_address), true)
                    return false
                } else if (addressId.isEmpty()) {
                    requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_address), true)
                    return false
                }
            }
        }
        if (selectedPayment.isEmpty()) {
            requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_payment), true)
            return false
        } else if (!deliveryTypeIsSelected) {
            requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_delivery_type), true)
            return false
        } else if (!isExpressDeliverySelected) {
            if (orderSelectedDate.isEmpty()) {
                requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_date), true)
                return false
            }
            if (timePeriodsId == 0) {
                requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_time), true)
                return false
            }
        }
        return true
    }
    private fun checkout(execute: Int = 1) {
        val checkoutHashmap = HashMap<String, Any>()
        checkoutHashmap["copon_id"] = couponId
        checkoutHashmap["execute"] = execute
        checkoutHashmap["payment_method"] = selectedPayment
        checkoutHashmap["delivery_type"] = deliveryType
        if (!isExpressDeliverySelected) {
            checkoutHashmap["delivery_date"] = orderSelectedDate
            checkoutHashmap["delivery_period_id"] = timePeriodsId
        }
        if (isUser())
            checkoutHashmap["address_id"] = addressId
        else {
            checkoutHashmap["area_id"] = guestAddress?.areaId.toString()
            checkoutHashmap["address"] = guestAddress?.addressSummary.toString()
            checkoutHashmap["username"] = guestAddress?.customerName.toString()
            checkoutHashmap["email"] = guestAddress?.customerEmail.toString()
            checkoutHashmap["mobile"] = guestAddress?.customerPhone.toString()
        }

        viewModel.checkout(checkoutHashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        if (selectedPayment == "wallet") {
                            val bundle = Bundle()
                            val checkoutStatusRsm = CheckoutStatusRsm(it.order_id, true, true, "", "")
                            bundle.putParcelable(CHECKOUT_STATUS_OBJ, checkoutStatusRsm)
                            mainController.navigate(R.id.action_checkout_CheckoutStatus, bundle)
                        } else {
                            val bundle = Bundle()
                            bundle.putInt(ORDER_ID, it.order_id)
                            bundle.putString(PAYMENT_URL, it.paymentUrl)
                            mainController.navigate(R.id.action_checkout_payment, bundle)
                        }
                    }
                }
                FAILURE, ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }
            }
        })
    }

    //endregion
    // regionDeliveryType
    private fun initDeliveryTypesRv() {

        binding.rvDeliveryType.apply {
            linearLayoutManager()
            adapter = deliveryRvAdapter
        }
        deliveryRvAdapter.setOnClickListener { _, item, position ->
            if (item.deliveryPrice.isEmpty()) {
                if (addressList.isEmpty()) {
                    requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_empty_address), true)
                } else if (addressId.isEmpty()) {
                    requireActivity().setSnackbar(binding.btncheckout, getString(R.string.checkout_validation_select_address), true)

                }
            } else if (item.isEnabled) {

                deliveryTypeIsSelected = true
                if (position == 0) {
                    isExpressDeliverySelected = true
                    deliveryType = "express"
                    binding.apply {
                        cvDateTime.gone()
                        showViewWithAlphaAnimation(cvExpressDelivery)
                    }
                } else {
                    deliveryType = "usual"
                    isExpressDeliverySelected = false
                    binding.apply {
                        cvExpressDelivery.gone()
                        showViewWithAlphaAnimation(cvDateTime)
                    }
                }

                delivery = if (item.deliveryPrice.toDouble().roundToInt() == 0)
                    "0.000"
                else
                    df.format(item.deliveryPrice.toDouble()).toString()
                refreshPrices.value = true

                if (walletValue < total && selectedPayment == "wallet") {
                    selectedPayment = ""
                    paymentMethodRvAdapter.setTotalValue(total)
                }
            }
        }
    }

    //endregion
    //regionPaymentMethods
    private fun initPaymentMethodRv() {
        binding.rvPaymentMethod.apply {
            linearLayoutManager()
            adapter = paymentMethodRvAdapter
        }
        paymentMethodRvAdapter.setOnClickListener { _, _, position ->
            selectedPayment = when (position) {
                0 -> "knet"
                else -> "wallet"
            }
        }
    }

    //endregion
    //regionDate&Time
    private fun initDatesRv() {
        binding.rvDate.apply {
            linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = datesRvAdapter
            if (this.itemDecorationCount == 0)
                this.addItemDecoration(GridSpacingItemDecoration(5, 25, false))
        }
        datesRvAdapter.setOnClickListener { _, item, position ->
            orderSelectedDate = item.date
            timePeriodsId = 0
            if (position == 0) {
                timePeriodsRvAdapter.setFirstDayIsClicked(true)
            } else
                timePeriodsRvAdapter.setFirstDayIsClicked(false)


            showViewWithAlphaAnimation(binding.lnTimePeriods)

        }
    }


    private fun initTimePeriodsRv() {
        val gridLayout = GridLayoutManager(requireContext(), 2)
        binding.rvTime.apply {
            layoutManager = gridLayout
            adapter = timePeriodsRvAdapter
            if (this.itemDecorationCount == 0)
                this.addItemDecoration(GridSpacingItemDecoration(2, 30, false))
        }
        timePeriodsRvAdapter.setOnClickListener { _, item, _ ->
            timePeriodsId = item.id
        }
    }
    //endregion


    @SuppressLint("SetTextI18n")
    private fun getCheckoutDetails() {
        viewModel.getCheckoutDetails().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    if (!isUser()) {
                        binding.apply {
                            clRoot.visible()
                            shimmer.root.gone()
                        }
                    }
                    it?.data?.let {
                        checkoutDetailsRsm = it
                        isOrderFromMultipleVendor = it.expressDelivery != 1
                        deliveryRvAdapter.setIsOrderFromMultiVendor(isOrderFromMultipleVendor)
                        datesRvAdapter.setIsOrderFromMultiVendor(isOrderFromMultipleVendor)
                        paymentMethodRvAdapter.setWalletValue(roundDoublePrice(checkoutDetailsRsm.wallet).toDouble())
                        paymentMethodRvAdapter.setTotalValue(roundDoublePrice(checkoutDetailsRsm.total).toDouble())
                        binding.tvExpressDeliveryMsg.text = it.expressDeliveryMessage
                        binding.tvSelectDate.text = "${it.dates.start} ${getString(R.string.checkout_to)} ${it.dates.end}"
                        binding.tvCurrentYear.text = it.dates.year
                        datesRvAdapter.fill(it.dates.days)
                        timePeriodsRvAdapter.fill(it.periods)
                        walletValue = it.wallet.toDouble()
                    }
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun setObservers() {
        refreshPrices.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                total = (subTotal.toDouble() + delivery.toDouble()) - discount.toDouble()
                binding.tvTotal.text = getString(R.string.global_currency, df.format(total).toString())
                setPricesViews()
            }
        })
    }

    private fun setPricesViews() {
        binding.apply {
            tvTotal.text = getString(R.string.global_currency, df.format(total).toString())
            tvDeliveryCharge.text = getString(R.string.global_currency, delivery)
            tvProductTotal.text = getString(R.string.global_currency, df.format(subTotal.toDouble()).toString())
            tvDiscount.text = getString(R.string.global_currency, discount)
        }
    }


    private fun handleViewsListeners() {
        binding.apply {
            btnCouponApplied.setOnClickListener {
                requireContext().hideKeyboard(it)
                when {
                    etCoupon.isEmpty() -> {
                        etCoupon.requestFocus()
                        requireActivity().setSnackbar(binding.btnCouponApplied, getString(R.string.checkout_validation_empty_coupon), true)
                    }
                    //couponCode == etCoupon.text.toString() -> requireActivity().setSnackbar(binding.btnCouponApplied, getString(R.string.checkout_coupon_used), true)
                    else -> applyCoupon()
                }
            }
            etCoupon.onChange {
                binding.apply {
                    tvSuccessPromo.gone()
                    tvFailPromo.gone()
                }
            }
            flselectedAddress.setOnClickListener {
                spAddress.performClick()
            }
            tvAddAddress.setOnClickListener {
                mainController.navigate(R.id.action_checkout_addnewAddress)
            }

            btncheckout.setOnClickListener {
                requireContext().hideKeyboard(binding.btncheckout)

                if (validateCheckout())
                    checkout()

            }
        }
    }


    private fun applyCoupon() {
        viewModel.applyCoupon(binding.etCoupon.text?.trim().toString(), subTotal).observe(
                viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.used.let { isUsed ->
                        if (isUsed == 1) {
                            requireActivity().setSnackbar(binding.btnCouponApplied, it.message, true)
                            couponId = ""
                        } else {
                            binding.apply {
                                tvSuccessPromo.visible()
                                tvFailPromo.gone()
                                it?.data?.let {
                                    if (walletValue < total && selectedPayment == "wallet") {
                                        selectedPayment = ""
                                        paymentMethodRvAdapter.setTotalValue(total)
                                    }
                                    couponCode = etCoupon.text.toString()
                                    discount = df.format(it.discount).toString()
                                    couponId = it.coponId.toString()
                                    refreshPrices.value = true
                                }
                            }
                        }

                    }

                }
                ERROR -> {
                    discount = "0.000"
                    refreshPrices.value = true
                    dismissLoading()
                    binding.apply {
                        tvSuccessPromo.gone()
                        tvFailPromo.visible()
                    }
                    requireActivity().setSnackbar(binding.btnCouponApplied, it.message, true)
                    couponId = ""
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnCouponApplied, it.message)
                }
            }
        }
        )
    }

    private fun showViewWithAlphaAnimation(view: View) {
        if (view.isGone) {
            view.apply {
                if (this.isGone) {
                    visible()
                    val alpha = AlphaAnimation(0f, 1f)
                    alpha.duration = 800
                    this.startAnimation(alpha)

                }
            }

        }
    }

}
