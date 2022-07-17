package com.app.meatz.presentation.featureStores.categoryStores

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
import com.app.meatz.data.utils.extensions.load
import com.app.meatz.data.utils.extensions.showViewsAnimation
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentCategoryStoresBinding
import com.app.meatz.domain.local.CategoryStoreRsm
import com.app.meatz.domain.remote.Category
import com.app.meatz.domain.remote.generalResponse.Cart
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.app.meatz.domain.remote.stores.Ad
import com.app.meatz.domain.remote.stores.Stores
import com.app.meatz.presentation.featureStores.StoresViewModel
import com.app.meatz.presentation.home.MainActivity


class CategoryStoresFragment : BaseFragment<FragmentCategoryStoresBinding>() {
    private val categoryShopsAdapter by lazy { CategoryStoresRvAdapter() }
    private val viewModel by viewModels<StoresViewModel>()
    private val listIsEmpty: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    private var bannerListIsEmpty = false
    private lateinit var selectedCategory: Category
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Category>(SELECTED_CATEGORY)?.let {
            selectedCategory = it
            getStores()

        }

        initCategoryShopsRv()
        setObservers()

    }


    private fun getStores() {
        viewModel.getStores(selectedCategory.id).observe(viewLifecycleOwner, Observer {
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
                        setSelectedCategoryView()
                        setCartViews(it.cart)
                        setStoresViews(it)
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.tvItemsNumber, it?.message.toString(), true)
                }
                FAILURE -> {
                    binding.shimmer.root.gone()
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun setStoresViews(stores: Stores) {

        if (stores.ads.isEmpty())
            bannerListIsEmpty = true
        binding.apply {
            if (stores.storeData.isNotEmpty()) {
                listIsEmpty.value = false
                setStoresAdapter(stores.storeData, stores.ads)
                val storeSize = stores.storeData.size

                tvItemsNumber.text = getString(R.string.category_shops_available, storeSize.toString())

            } else {
                tvItemsNumber.text = getString(R.string.category_shops_available, "0")
                listIsEmpty.value = true

            }


        }
    }

    private fun setStoresAdapter(storesList: List<StoreData>, bannerList: List<Ad>) {
        //before all of that we will check if product is empty we will not call of theses
        //setArraySizes
        val storesAndBanerList: Array<CategoryStoreRsm?>
        var arraySize = if (bannerList.isEmpty())
            storesList.size
        else
            storesList.size + (storesList.size / 6.toInt())
        storesAndBanerList = arrayOfNulls(arraySize)

        //fillItemIndexes
        //fillBannerIndexes

        val storesIndexes: ArrayList<Int> = ArrayList()
        val bannerIndexes: ArrayList<Int> = ArrayList()

        //fillBannerAtSpecialIndexes
        for (i in 6 until arraySize step 7)
            bannerIndexes.add(i)
        if (bannerList.isNotEmpty() && bannerIndexes.isNotEmpty())
            bannerIndexes.forEachIndexed { index, i ->
                storesAndBanerList[i] = CategoryStoreRsm(BANNER_TYPE, bannerUrl = bannerList[index].image, bannerType = bannerList[index].model, bannerItemId = bannerList[index].modelId)
            }


        //fillItemsAtSpecialIndexes
        storesAndBanerList.forEachIndexed { index, i ->
            if (i == null)
                storesIndexes.add(index)
        }

        storesIndexes.forEachIndexed { index, parentIndex ->
            storesAndBanerList[parentIndex] =
                    CategoryStoreRsm(ITEM_TYPE, storeUrl = storesList[index].logo, storeName = storesList[index].name, storeId = storesList[index].id)
        }

        categoryShopsAdapter.fill(storesAndBanerList.toList() as List<CategoryStoreRsm>)

    }

    private fun setSelectedCategoryView() {
        binding.apply {
            ivCategory.load(selectedCategory.image)
            tvCatTitle.text = selectedCategory.name
        }

    }

    private fun setCartViews(cart: Cart) {
        if (cart.count != 0) {
            binding.apply {
                showViewsAnimation(rlCart)
                rlCart.visible()
                tvCartCount.text = cart.count.toString()
                tvCartPrice.text = getString(R.string.global_currency, cart.total.toString())
                tvViewCart.setOnClickListener {
                    mainController.navigate(R.id.action_categoryStores_cart)
                }
            }

        }
    }


    private fun setObservers() {
        listIsEmpty.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    binding.apply {
                        rvCategoryShops.gone()
                        emptyLayout.visible()
                    }
                }
                else -> {
                    binding.apply {
                        rvCategoryShops.visible()
                        emptyLayout.gone()

                    }
                }
            }
        })
    }

    private fun initCategoryShopsRv() {
        val gridlayout = GridLayoutManager(activity, 1)
        binding.rvCategoryShops.apply {
            layoutManager = gridlayout
            adapter = categoryShopsAdapter
        }
        gridlayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (bannerListIsEmpty)
                    return 1
                else {

                    return if (categoryShopsAdapter.getItemType(position) == ITEM_TYPE) {
                        1
                    } else
                        3
                }
            }
        }

        categoryShopsAdapter.setOnClickListener { _, item, _ ->
            when (item.itemType) {
                ITEM_TYPE -> {
                    val bundle = Bundle()


                    bundle.putInt(SHOP_ID, item.storeId)
                    mainController.navigate(R.id.action_categoryStores_storeDetails, bundle)
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
                mainController.navigate(R.id.action_categoryStores_productDetails, bundle)
            }
            BANNER_BOX -> {
                bundle.putInt(BOX_ID, bannerTypeId)
                mainController.navigate(R.id.action_categoryStores_boxDetails, bundle)
            }
            BANNER_STORE -> {
                bundle.putInt(SHOP_ID, bannerTypeId)
                mainController.navigate(R.id.action_categoryStores_storeDetails, bundle)
            }
        }
    }
}