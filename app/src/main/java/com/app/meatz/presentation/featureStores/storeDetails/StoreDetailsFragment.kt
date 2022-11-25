package com.app.meatz.presentation.featureStores.storeDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.*
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentStoreDetailsBinding
import com.app.meatz.domain.local.ProductsRsm
import com.app.meatz.domain.remote.generalResponse.Banner
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.shopDetails.CatProducts
import com.app.meatz.domain.remote.shopDetails.StoreCategory
import com.app.meatz.domain.remote.shopDetails.StoreDetails
import com.app.meatz.presentation.featureStores.StoresViewModel
import com.app.meatz.presentation.featureStores.storeDetails.adapter.ParentAdapter
import com.app.meatz.presentation.featureStores.storeDetails.adapter.StoreProductsRvAdapter
import com.app.meatz.presentation.home.MainActivity
import com.google.android.material.tabs.TabLayout


class StoreDetailsFragment : BaseFragment<FragmentStoreDetailsBinding>() {

    private val shopProductsAdapter by lazy { StoreProductsRvAdapter() }
    private val filterDialog by lazy { FilterDialog(requireContext()) }
    private val sortDialog by lazy { SortDialog(requireContext()) }
    private val viewModel by viewModels<StoresViewModel>()
    private var categoryList  = ArrayList<StoreCategory>()
    private val listIsEmpty: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    private var bannerListIsEmpty = false
    private var storeId = 0
    private var sortKeyWord = ""
    private var filterCategoryId = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            storeId = it.getInt(SHOP_ID)
        }

        revokeRecalling {
            getStoreDetails()
        }

        handleClicks()
        setObservers()
    }


    private fun getStoreDetails() {
        viewModel.getStoreDetails(storeId, filterCategoryId, sortKeyWord).observe(viewLifecycleOwner, Observer {
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
                        initStoreView(it)
                        categoryList.addAll(it.categories)
                        for (i in 0..it.store.products.size -1){
                            binding.shopTabLayout.addTab(binding.shopTabLayout.newTab().setText(it.store.products[i].subCatName))
                        }
                        binding.shopTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabSelected(tab: TabLayout.Tab?) {
                                binding.rvProducts.scrollToPosition(tab!!.position)
                            }

                            override fun onTabUnselected(tab: TabLayout.Tab?) {

                            }

                            override fun onTabReselected(tab: TabLayout.Tab?) {

                            }
                        })
                        initShopProductsRv(it.store.products)
                        // filterDialog.setCategoriesList(it.categories as ArrayList<StoreCategory>)
                    }

                }
                ERROR -> {
                    binding.apply {
                        shimmer.root.gone()
                        clRoot.gone()
                    }
                    requireActivity().setSnackbar(binding.cdShop, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }

                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initStoreView(storeDetails: StoreDetails) {
        if (storeDetails.banners.isEmpty())
            bannerListIsEmpty = true
        binding.apply {
            ivShop.loadWithPlaceHolder(storeDetails.store.logo)
            bannerShop.loadWithPlaceHolder(storeDetails.store.banner)
            tvShopTitle.text = storeDetails.store.name
            if (storeDetails.store.products.isNotEmpty()) {
                listIsEmpty.value = false
                setProductsAdapter(storeDetails.store.products[0].products, storeDetails.banners)
                val storeSize = storeDetails.store.products.size
                    tvItemsNumber.text = "${storeSize} ${getString(R.string.shop_details_items)}"

            } else {
                tvItemsNumber.text = "0 ${getString(R.string.shop_details_items)}"
                listIsEmpty.value = true
            }
        }
    }

    private fun setProductsAdapter(productList: List<ProductData>, bannerList: List<Banner>) {
        //before all of that we will check if product is empty we will not call of theses
        //setArraySizes
        val productAndBanerList: Array<ProductsRsm?>
        var arraySize = if (bannerList.isEmpty())
            productList.size
        else
            productList.size + (productList.size / 6.toInt())
        productAndBanerList = arrayOfNulls(arraySize)


        //fillItemIndexes
        //fillBannerIndexes
        val productsIndexes: ArrayList<Int> = ArrayList()
        val bannerIndexes: ArrayList<Int> = ArrayList()

        //fillBannerAtSpecialIndexes

        for (i in 6 until arraySize step 7)
            bannerIndexes.add(i)
        if (bannerList.isNotEmpty() && bannerIndexes.isNotEmpty())
            bannerIndexes.forEachIndexed { index, i ->
                productAndBanerList[i] = ProductsRsm(BANNER_TYPE, bannerUrl = bannerList[index].image, bannerType = bannerList[index].model, bannerItemId = bannerList[index].modelId, productType = "")
            }


        //fillItemsAtSpecialIndexes
        productAndBanerList.forEachIndexed { index, i ->
            if (i == null)
                productsIndexes.add(index)
        }
        productsIndexes.forEachIndexed { index, parentIndex ->
            productAndBanerList[parentIndex] =
                    ProductsRsm(ITEM_TYPE, productId = productList[index].id, productName = productList[index].name,
                            productUrl = productList[index].image, productPrice = productList[index].price,
                            prdocutOldPrice = productList[index].priceBefore, productType = productList[index].type)
        }

        shopProductsAdapter.fill(productAndBanerList.toList() as List<ProductsRsm>)

    }

    private fun setObservers() {
        listIsEmpty.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    binding.apply {
                        rvProducts.gone()
                        emptyLayout.visible()
                    }
                }
                else -> {
                    binding.apply {
                        rvProducts.visible()
                        emptyLayout.gone()
                    }
                }
            }
        })
    }

    private fun initShopProductsRv(catProducts: List<CatProducts>) {
        val gridlayout = GridLayoutManager(activity, 1)
        val parentAdapter = ParentAdapter(catProducts)
        binding.rvProducts.apply {
            layoutManager = gridlayout
            adapter = parentAdapter
        }
        gridlayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (bannerListIsEmpty)
                    return 1
                else {
                    return if (shopProductsAdapter.getItemType(position) == ITEM_TYPE)
                        1
                    else
                        1
                }

            }


        }

        parentAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putInt(PRODUCT_ID, it)
            mainController.navigate(R.id.action_storeDetails_productDetails, bundle)
        }

        shopProductsAdapter.setOnClickListener { _, item, _ ->
            when (item.itemType) {
                ITEM_TYPE -> {
                    val bundle = Bundle()
                    if (item.productType == ITEM_SPECIAL_BOX) {
                        bundle.putInt(BOX_ID, item.productId)
                        mainController.navigate(R.id.action_storeDetails_specialBoxDetails, bundle)
                    } else {
                        bundle.putInt(PRODUCT_ID, item.productId)
                        mainController.navigate(R.id.action_storeDetails_productDetails, bundle)
                    }
                }
                else -> {
                    handleClickonBanner(item.bannerType, item.bannerItemId)
                }
            }


        }
    }

    private fun handleClickonBanner(bannerType: String, bannerTypeId: Int) {
        val bundle = Bundle()

        when (bannerType) {
            BANNER_PRODUCT -> {
                bundle.putInt(PRODUCT_ID, bannerTypeId)
                mainController.navigate(R.id.action_storeDetails_productDetails, bundle)
            }
            BANNER_BOX -> {
                bundle.putInt(BOX_ID, bannerTypeId)
                mainController.navigate(R.id.action_storeDetails_boxDetails, bundle)
            }
            BANNER_STORE -> {
                //to check if you are at the same restaurant so no need to navigatie to the same rest
                if (bannerTypeId != storeId) {
                    bundle.putInt(SHOP_ID, bannerTypeId)
                    mainController.navigate(R.id.action_storeDetails_storeDetails, bundle)
                }
            }
        }
    }

    private fun handleClicks() {
        binding.apply {
            ivFilter.setOnClickListener {
                filterDialog.show()
                filterDialog.performSubmit {
                    filterCategoryId = ""
                    filterDialog.filterSelectedCategory.forEach {
                        filterCategoryId += "$it,"
                    }
                    if (filterDialog.filterSelectedCategory.isNotEmpty())
                        getStoreDetails()
                    filterDialog.dismiss()
                }
            }

            ivSort.setOnClickListener {
                sortDialog.show()
                sortDialog.performSubmit {
                    if (sortDialog.getSortSelected.isNotEmpty()) {
                        sortKeyWord = sortDialog.getSortSelected
                        getStoreDetails()
                    }
                    sortDialog.dismiss()
                }
            }
        }
    }
}