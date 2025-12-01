package com.app.meatz.presentation.events

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.CARNIVAL_ID
import com.app.meatz.data.application.DISABLE_SELECTED_VIEW_OPTIONS
import com.app.meatz.data.application.PRODUCT_ID
import com.app.meatz.data.application.SELECTED_CATEGORY
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.loadWithPlaceHolder
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentEventBinding
import com.app.meatz.domain.remote.Carnival
import com.app.meatz.presentation.events.ViewModel.EventDetailViewModel
import com.app.meatz.presentation.events.adapter.PartnerAdapter
import com.app.meatz.presentation.featureStores.storeDetails.adapter.ParentAdapter
import com.app.meatz.presentation.home.HomeViewModel
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.shared.MainCategoryRvAdapter
import com.google.gson.Gson

class EventDetailFragment: BaseFragment<FragmentEventBinding>() {
    private val viewModel by viewModels<EventDetailViewModel>()
    private val partnerAdapter by lazy { PartnerAdapter() }
    private var carnivalId = 0
    private var carnivalTitle = ""
    private var carnivalEndDate = ""
    private lateinit var carnivalDetail: Carnival

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            carnivalId = it.getInt(CARNIVAL_ID)
        }
        getCarnivalDetail()
        initPartnerRv()
        handleViewsClick()

    }

    fun handleViewsClick(){
        val bundle = Bundle()
        binding.reservationCard.setOnClickListener{
            bundle.putInt(CARNIVAL_ID,carnivalId)
            bundle.putString("endDate",carnivalEndDate)
            bundle.putString("carnivalTitle",carnivalTitle)
            bundle.putParcelable("carnivalDetail",carnivalDetail)
            mainController.navigate(R.id.action_add_event_detail,bundle)
        }
    }

    private fun getCarnivalDetail(){

        viewModel.getCarnivalDetail(carnivalId).observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                LOADING ->
                    binding.apply {

                    }

                SUCCESS -> {
                    binding.apply {

                    }

                    it?.data?.let {
                        carnivalDetail = it
                        carnivalEndDate = it.endDate
                        carnivalTitle = it.title
                        partnerAdapter.fill(it.partners)
                        it.slider.image?.let { it1 -> binding.eventImage.loadWithPlaceHolder(it1) }
                        binding.eventHours.text = it.hours
                        binding.eventlocation.text = it.location
                        binding.eventdescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(it.content, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(it.content)
                        }
                    }

                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.contactuslabel, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }

                }
            }
        })

    }

    private fun initPartnerRv() {
        binding.rvPartner.apply {
            linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = partnerAdapter
        }
    }

}