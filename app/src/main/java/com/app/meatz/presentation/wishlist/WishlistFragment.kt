package com.app.meatz.presentation.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.BOX_ID
import com.app.meatz.data.application.ITEM_BOX_TYPE
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentWishlistBinding
import com.app.meatz.presentation.home.MainActivity


class WishlistFragment : BaseFragment<FragmentWishlistBinding>() {

    private val viewModel by viewModels<WishlistViewModel>()
    private val wishlistRvAdapter by lazy { WishlistRvAdapter() }
    private val wishListIsEmpty by lazy { MutableLiveData<Boolean>() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revokeRecalling {
            initFavouriteRv()
            getWishlist(1)
        }
        setObservers()

    }

    private fun setObservers() {
        wishListIsEmpty.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    binding.apply {
                        rvWishlist.gone()
                        emptyLayout.visible()
                    }
                }
                else -> {
                    binding.apply {
                        rvWishlist.visible()
                        emptyLayout.gone()
                    }
                }
            }
        })
    }

    private fun getWishlist(page: Int, showShimmer: Boolean = true) {
        viewModel.getWishlist(page).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> if (page == 1) {
                    if (showShimmer) {
                        binding.apply {
                            rlRoot.gone()
                            shimmer.root.visible()
                        }
                    }
                }
                SUCCESS -> {
                    binding.apply {
                        rlRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        if (page == 1) {
                            if (it.isEmpty())
                                wishListIsEmpty.value = true
                            else {
                                wishListIsEmpty.value = false
                                wishlistRvAdapter.fill(it)
                            }
                        } else
                            wishlistRvAdapter.addItems(it)
                    }

                    it?.pagination?.let { pagination ->
                        if (pagination.currentPage == pagination.lastPage) wishlistRvAdapter.setLoaded()
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.tvEmpty, it?.message.toString(), true)
                }
                FAILURE -> {

                    if (page == 1) {
                        handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                    } else
                        requireActivity().setSnackbar(binding.emptyLayout, it.message)
                }
            }
        })
    }


    private fun initFavouriteRv() {
        val gridlayout = GridLayoutManager(activity, 2)
        binding.rvWishlist.apply {
            layoutManager = gridlayout
            adapter = wishlistRvAdapter

        }
        wishlistRvAdapter.apply {
            setOnLoadMoreListener { getWishlist(it) }
            setOnClickListener { itemview, item, _ ->
                when (itemview.id) {
                    R.id.ivFavourite -> {
                        showRemoveItemDialog(item.id)
                    }
                    else -> {
                        val bundle = Bundle()
                        if (item.type == ITEM_BOX_TYPE) {
                            bundle.putInt(BOX_ID, item.id)
                            mainController.navigate(R.id.action_wishlist_boxDetails, bundle)
                        } else {
                            bundle.putInt(PRODUCT_ID, item.id)
                            mainController.navigate(R.id.action_wishlist_productDetails, bundle)
                        }
                    }
                }
            }
        }

    }

    private fun showRemoveItemDialog(productId: Int) {
        showAlert(getString(R.string.wishlist_remove_item), getString(R.string.wishlist_dialog_msg),
                getString(R.string.base_ok), { removeWishListItem(productId) }, true, true)
    }

    private fun removeWishListItem(productId: Int) {
        viewModel.removeFromWishlist(productId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    getWishlist(1, false)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.rvWishlist, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.emptyLayout, it.message)
                }
            }
        })
    }
}