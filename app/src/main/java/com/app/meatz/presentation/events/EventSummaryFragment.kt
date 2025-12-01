package com.app.meatz.presentation.events

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.app.meatz.core.BaseFragment
import com.app.meatz.databinding.FragmentEventSummaryBinding
import com.app.meatz.domain.remote.AddEvent
import com.app.meatz.presentation.events.ViewModel.AddEventViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.data.application.BOOKING_ID
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.application.PAYMENT_URL
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.domain.remote.PaymentInfo.PaymentInfo
import com.app.meatz.domain.remote.orderDetails.Payment

class EventSummaryFragment: BaseFragment<FragmentEventSummaryBinding>() {

    private val viewModel by viewModels<AddEventViewModel>()
    private val hashmap: HashMap<String, Any> by lazy { HashMap() }

    var eventDetail = AddEvent()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
             eventDetail = it.getParcelable<AddEvent>("eventDetail")!!
        }
        loaddata()
        handleViewsClick()
    }

    private fun handleViewsClick(){
        binding.tvPayToConfirm.setOnClickListener{
            carnivalBooking()
        }
    }

    private fun loaddata(){
        binding.apply {
            tvEventName.text = eventDetail.carnivalTitle
            tvEventPrice.text = eventDetail.ticketPrice
            tvEventDate.text = eventDetail.date
            tvKhashtaSize.text = "For ${eventDetail.khashtaType.dropLast(6)} Khashta "
        }
    }

    private fun fillCarnivalBookingHashmap() {
        hashmap.clear()
        binding.apply {
            hashmap["carnival_id"] = eventDetail.carnivalId
            hashmap["carnival_ticket_type_id"] = eventDetail.ticketID
            hashmap["carnival_khashta_size_id"] = eventDetail.khashtaSizeId
            hashmap["date"] = eventDetail.date
            hashmap["customer_name"] = eventDetail.name
            hashmap["customer_email"] = eventDetail.email
            hashmap["customer_mobile_number"] = eventDetail.mobile
            hashmap["special_request"] = eventDetail.specailRequest
            hashmap["payment_type"] = "knet"
            hashmap["amount"] = eventDetail.price
        }

    }

    private fun carnivalBooking(){
        fillCarnivalBookingHashmap()
        viewModel.carnivalBooking(hashmap).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    if (it?.data != null) {
                        val bundle = Bundle()
                        bundle.putInt(ORDER_ID, it.data.bookingId)
                        bundle.putString(PAYMENT_URL, it.data.paymentUrl)
                        mainController.navigate(R.id.action_checkout_payment, bundle)
                    } else
                        requireActivity().setSnackbar(binding.tvPayToConfirm, it.message, true)
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.tvPayToConfirm, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.tvPayToConfirm, it.message)
                }
            }
        })
    }
}