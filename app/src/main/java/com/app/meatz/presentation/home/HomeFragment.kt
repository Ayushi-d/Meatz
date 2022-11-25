package com.app.meatz.presentation.home

import android.app.LauncherActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.*
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentHomeBinding
import com.app.meatz.domain.remote.Boxes
import com.app.meatz.domain.remote.Featured
import com.app.meatz.domain.remote.Slider
import com.app.meatz.presentation.home.adapter.BannerVpAdapter
import com.app.meatz.presentation.home.adapter.OurBoxRvAdapter
import com.app.meatz.presentation.home.adapter.OurSelectionRvAdapter
import com.app.meatz.presentation.shared.MainCategoryRvAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()
    private val mainCategoryAdapter by lazy { MainCategoryRvAdapter(DISABLE_SELECTED_VIEW_OPTIONS) }
    private val bannerVPAdapter by lazy { BannerVpAdapter() }
    private val ourSelectionRvAdapter by lazy { OurSelectionRvAdapter() }
    private val ourBoxRvAdapter by lazy { OurBoxRvAdapter() }
    val handler = Handler()
    var origPosition: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getHome()
        initMainCategoryRv()
        handleViewsClicks()
        setLineWidth(binding.tvOurSelection, binding.line1)
        setLineWidth(binding.tvOurBoxes, binding.line2)
        swipeRefresh()

    }


    private fun swipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            getHome()
        }
    }


    private fun getHome() {

        viewModel.getHome().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING ->
                    binding.apply {
                        shimmer.root.visible()
                        clRoot.gone()
                    }

                SUCCESS -> {
                    binding.apply {
                        shimmer.root.gone()
                        clRoot.visible()
                    }

                    it?.data?.let {
                        mainCategoryAdapter.fill(it.categories)
                        initBannerPager(it.sliders)
                        initOurSelectionRv(it.featured)
                        initOurBoxRv(it.boxes)
                    }

                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.rlOurBoxes, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }

                }
            }
        })
    }


    private fun initMainCategoryRv() {
        binding.rvMainCategory.apply {
            linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = mainCategoryAdapter
        }


        mainCategoryAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putParcelable(SELECTED_CATEGORY, item)
            mainController.navigate(R.id.action_home_categoryShops, bundle)
        }
    }



    private fun initBannerPager(sliderList: List<Slider>) {
        bannerVPAdapter.fill(sliderList)
        when {
            sliderList.isEmpty() -> {
                binding.vpBanner.gone()
                binding.tabBanner.gone()
            }
            else -> {
                binding.vpBanner.visible()
                binding.vpBanner.apply {
                    adapter = bannerVPAdapter
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }

                TabLayoutMediator(binding.tabBanner, binding.vpBanner) { _, _ -> }.attach()
            }
        }

        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val runnable = Runnable {
                    binding.vpBanner.setCurrentItem(position + 1)
                    if (position == sliderList.size -1){
                        binding.vpBanner.currentItem = 0
                    }
                }
                if (position < binding.vpBanner.adapter?.itemCount ?: 0) {
                    handler.postDelayed(runnable, 3000)
                }
            }
        })


        bannerVPAdapter.setOnClickListener { _, item, _ ->

            val bundle = Bundle()
            when (item.model) {
                BANNER_PRODUCT -> {
                    item.modelId?.let { bundle.putInt(PRODUCT_ID, it) }
                    mainController.navigate(R.id.action_home_productDetails, bundle)
                }
                BANNER_BOX -> {
                    item.modelId?.let { bundle.putInt(BOX_ID, it) }
                    mainController.navigate(R.id.action_home_boxDetails, bundle)
                }
                BANNER_STORE -> {
                    item.modelId?.let { bundle.putInt(SHOP_ID, it) }
                    mainController.navigate(R.id.action_home_shopDetails, bundle)
                }
            }

        }
    }

    private fun initOurSelectionRv(featuredList: List<Featured>) {
        ourSelectionRvAdapter.fill(featuredList)
        if (featuredList.isEmpty())
            binding.rlOurSelection.gone()
        else
            binding.rvOurselection.apply {
                linearLayoutManager(RecyclerView.HORIZONTAL)
                adapter = ourSelectionRvAdapter
            }

        ourSelectionRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(SHOP_ID, item.id)
            mainController.navigate(R.id.action_home_shopDetails, bundle)
        }
    }

    private fun initOurBoxRv(ourBoxesList: List<Boxes>) {
        ourBoxRvAdapter.fill(ourBoxesList)
        if (ourBoxesList.isEmpty()) {
            binding.apply {
                cdEmptyBox.visible()
                rvOurboxes.gone()
                tvViewallboxes.gone()
            }
        } else

            binding.rvOurboxes.apply {
                linearLayoutManager(RecyclerView.HORIZONTAL)
                adapter = ourBoxRvAdapter
            }

        ourBoxRvAdapter.setOnClickListener { _, item, _ ->
            val bundle = Bundle()
            bundle.putInt(BOX_ID, item.id)
            mainController.navigate(R.id.action_home_boxDetails, bundle)
        }
    }


    private fun setLineWidth(aboveTitle: TextView, line: View) {
        val params: RelativeLayout.LayoutParams = line.layoutParams as RelativeLayout.LayoutParams
        aboveTitle.measure(0, 0)
        params.width = (aboveTitle.measuredWidth) / 2
        line.layoutParams = params
    }

    private fun handleViewsClicks() {
        binding.apply {
            tvViewallSelection.setOnClickListener {
                mainController.navigate(R.id.action_home_ourSelection)
            }
            tvViewallboxes.setOnClickListener {
                mainController.navigate(R.id.action_home_ourBoxes)
            }

            flSearch.setOnClickListener { mainController.navigate(R.id.action_home_search) }
            ivNewBox.setOnClickListener {
                if (viewModel.isUserLogged())
                    mainController.navigate(R.id.action_home_newBox)
                else
                    requireActivity().setSnackbar(binding.rlOurBoxes, getString(R.string.profile_login_first_msg), true)
            }
        }
    }


}