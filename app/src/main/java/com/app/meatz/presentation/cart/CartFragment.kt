package com.app.meatz.presentation.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.SUBTOTAL
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.preferences.user
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentCartBinding
import com.app.meatz.domain.remote.Cart
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.presentation.cart.dialog.AddPhoneNumberDialog
import com.app.meatz.presentation.cart.dialog.ContinueGuestDialog
import com.app.meatz.presentation.home.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CartFragment : BaseFragment<FragmentCartBinding>() {

    private val viewModel by viewModels<CartViewModel>()
    private val cartRvAdapter by lazy { CartRvAdapter() }
    private val continueAsGuestDialog by lazy { ContinueGuestDialog(requireContext()) }
    private val addPhoneNumberDialog by lazy { AddPhoneNumberDialog(requireContext()) }
    private var subTotal = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCart()
        handleViewsClick()
    }

    private fun getCart() {
        viewModel.getCart().observe(viewLifecycleOwner, Observer {
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
                    it?.data?.let { cart ->
                        subTotal = cart.subtotal
                        refreshCartViews(cart)
                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.btncheckout, it.message, true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })

    }

    private fun refreshCartViews(cart: Cart) {
        cart.let {
            if (it.products.isNotEmpty()) {
                binding.tvItemsNumber.text = getString(R.string.cart_number, it.products.size.toString())
            } else
                binding.tvItemsNumber.text = getString(R.string.cart_number, "0")
            initCartRv(it.products)
            binding.tvSubTotal.text = getString(R.string.global_currency, it.subtotal)

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCartRv(productsList: List<ProductData>) {
        if (productsList.isEmpty()) {
            binding.apply {
                txtSubTotal.gone()
                btncheckout.gone()
                tvSubTotal.gone()
                rvCart.gone()
                emptyLayout.visible()
            }
        } else {
            cartRvAdapter.fill(productsList)
            binding.apply {
                txtSubTotal.visible()
                btncheckout.visible()
                tvSubTotal.visible()
                rvCart.visible()
                emptyLayout.gone()
                rvCart.apply {
                    linearLayoutManager()
                    adapter = cartRvAdapter
                }
            }
            cartRvAdapter.setOnClickListener { itemView, item, i ->
                when (itemView.id) {
                    R.id.flRemoveProduct, R.id.flRemoveBox -> {
                        removeProductFromCart(item.cart_id)
                    }
                    R.id.tvProductPlus, R.id.tvBoxPlus -> {
                        if (item.count < item.num) {
                            item.count = item.count + 1
                            cartRvAdapter.notifyDataSetChanged()
                            fillCartUpdatesData(item, i)
                        } else
                            requireActivity().setSnackbar(binding.btncheckout, getString(R.string.box_details_max_quantity), true)

                    }
                    R.id.tvProductMinus, R.id.tvBoxMinus -> {
                        if (item.count != 1 && item.count != 0) {
                            item.count = item.count - 1
                            cartRvAdapter.notifyDataSetChanged()
                            fillCartUpdatesData(item, i)
                        }
                    }
                }
            }
        }
    }

    private fun removeProductFromCart(productId: Int) {
        viewModel.removeFromCart(productId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message, true)
                    it?.data?.let {
                        subTotal = it.subtotal
                        refreshCartViews(it)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }
            }
        })
    }

    fun fillCartUpdatesData(item: ProductData, position: Int) {
        val hashmap by lazy { HashMap<String, Any>() }
        val optionsId = ArrayList<Int>()
        if (item.options.isNotEmpty()) {
            item.options.forEach {
                optionsId.add(it.id)
            }
            hashmap["options"] = optionsId.joinToString(",") { it -> it.toString().trim() }
        }
        hashmap["product_id"] = item.id
        hashmap["count"] = item.count

        Log.i("updatedCart", hashmap.entries.toString())
        updateItems(hashmap, position)
    }

    fun updateItems(hashmap: HashMap<String, Any>, position: Int) {
        viewModel.updateCartItemsCount(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    it?.data?.let {
                        subTotal = it.subtotal
                        refreshCartViews(it)
                        binding.rvCart.scrollToPosition(position)

                    }
                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }

            }
        })
    }

    private fun handleViewsClick() {
        binding.btncheckout.setOnClickListener {
            checkOutOfStockProducts()
        }
    }

    private fun processedCheckout() {
        if (!viewModel.isUserLogged()) {
            continueAsGuestDialog.show()
            continueAsGuestDialog.setonCheckoutClick {
                val bundle = Bundle()
                bundle.putString(SUBTOTAL, subTotal)
                mainController.navigate(R.id.action_cart_guestInfo, bundle)
            }
        } else {
            val userMobile = viewModel.getUser()?.mobile
            if (userMobile == null || userMobile.isEmpty()) {
                addPhoneNumberDialog.show()
                    addPhoneNumberDialog.clickOnAddPhoneBtn {
                        addPhoneNumber(addPhoneNumberDialog.getPhoneNumber())
                    }
            } else {
                val bundle = Bundle()
                bundle.putString(SUBTOTAL, subTotal)
                mainController.navigate(R.id.action_cart_checkout, bundle)
            }

        }
    }

    private fun checkOutOfStockProducts() {
        viewModel.getCart().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        subTotal = it.subtotal
                        if (it.can_checkout == 1)
                            processedCheckout()
                        else {
                            val outOfStockItems = it.out_of_stock.joinToString { it -> it }
                            showOutOfStockDialog(outOfStockItems)
                        }

                    }
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }

            }
        })
    }

    private fun showOutOfStockDialog(items: String) {
        showAlert(getString(R.string.cart_out_of_stock), getString(R.string.cart_out_stock_dialog_msg, items),
                getString(R.string.cart_update), { clearOutOfStockItems() }, true, stanchedOnTouch = true)
    }

    private fun clearOutOfStockItems() {
        viewModel.clearOutOfStockItems().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        subTotal = it.subtotal
                        refreshCartViews(it)
                        if (it.products.isNotEmpty())
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(1000)
                                processedCheckout()
                            }
                    }
                }
                ERROR, FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }
            }
        })
    }

    private fun addPhoneNumber(phoneNumber: String) {
        val hashmap: HashMap<String, Any> by lazy { HashMap() }
        hashmap["first_name"] = user?.firstName.toString()
        hashmap["last_name"] = user?.lastName.toString()
        hashmap["mobile"] = phoneNumber
        hashmap["email"] = user?.email.toString()
        viewModel.editProfile(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    it?.data?.let {
                        viewModel.cacheUserData(it)
                    }

                    val bundle = Bundle()
                    bundle.putString(SUBTOTAL, subTotal)
                    mainController.navigate(R.id.action_cart_checkout, bundle)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message, true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btncheckout, it.message)
                }
            }
        })
    }
}