package com.app.meatz.presentation.events

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.viewModels
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.CARNIVAL_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.preferences.isUser
import com.app.meatz.data.utils.extensions.*
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentAddeventdetailsBinding
import com.app.meatz.domain.remote.AddEvent
import com.app.meatz.domain.remote.Carnival
import com.app.meatz.domain.remote.CarnivalKhashtaSize
import com.app.meatz.domain.remote.CarnivalTickets
import com.app.meatz.presentation.events.ViewModel.AddEventViewModel
import com.app.meatz.presentation.events.adapter.KhashtaAdapter
import com.app.meatz.presentation.events.adapter.TicketAdapter
import com.app.meatz.presentation.home.MainActivity
import okhttp3.internal.notify
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddEventDetailFragment : BaseFragment<FragmentAddeventdetailsBinding>() {

    private val viewModel by viewModels<AddEventViewModel>()
    private lateinit var ticketList: List<CarnivalTickets>
    private lateinit var khashtaList: List<CarnivalKhashtaSize>
    private lateinit var carnivalDetail: Carnival
    private var endDate = ""
    private var carnivalTitle = ""
    private var tickedId = 0
    private var ticketPrice = ""
    private var khashtaSizeId = 0
    private var isTicektSelected = false
    private var isKhashtaSizeselected = false
    private var isDateSelected = false
    private var carnivalId = 0
    private var dateToSend = "0"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            carnivalTitle = it.getString("carnivalTitle").toString()
            carnivalId = it.getInt(CARNIVAL_ID)
            endDate = it.getString("endDate").toString()
            carnivalDetail = it.getParcelable<Carnival>("carnivalDetail")!!
        }
        setUpCalendar()
        getTicketTypes()
        getkhashtaSizes()
        handleViewsClick()
    }

    private fun setUpCalendar(){
        binding.calendarView.gone()
        binding.calendarView.minDate = Calendar.getInstance().time.time
        binding.calendarView.maxDate = SimpleDateFormat("yyyy-MM-dd").parse(endDate).time
//        for(khashtaSizeItemsModel in carnivalDetail.carnivalKhashtaSizes){
//            for(daysData in khashtaSizeItemsModel.days){
//                if (khashtaSizeId == daysData.carnivalKhashtaSizeId){
//                    if (daysData.isDayOff == "1" || daysData.isBookingFull == "1"){
//                        //binding.calendarView.dateTextAppearance = R.color.grayx
//                    }else{
//                       /// binding.calendarView.dateTextAppearance = R.color.black
//                    }
//                }
//            }
//            //binding.calendarView.invalidate()
//        }
    }

    private fun getTicketTypes(){
        viewModel.getCarnivalTicket().observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            when (it.status) {
                LOADING ->
                    binding.apply {

                    }

                SUCCESS -> {
                    binding.apply {

                    }

                    it?.data?.let {
                        ticketList = it
                        setTicketSpinner(ticketList as ArrayList<CarnivalTickets>)
                    }

                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.tvNext, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }

                }
            }
        })
    }

    private fun getkhashtaSizes(){
        viewModel.getCarnivalKhashtaSize().observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            when (it.status) {
                LOADING ->
                    binding.apply {

                    }

                SUCCESS -> {
                    binding.apply {

                    }

                    it?.data?.let {
                        khashtaList = it
                        setKhashtaSpinner(khashtaList  as ArrayList<CarnivalKhashtaSize>)
                    }

                }
                ERROR -> {
                    requireActivity().setSnackbar(binding.tvNext, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }

                }
            }
        })
    }

    private fun handleViewsClick(){
        binding.flselectedTicket.setOnClickListener{
            binding.spTicket.performClick()
        }

        binding.dateCard.setOnClickListener{
            binding.calendarView.visible()
        }

        binding.khashtaCard.setOnClickListener{
            binding.spKhashta.performClick()
        }

        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            binding.tvDate.text = "$dayOfMonth-${month+1}-$year"
            dateToSend = "$year-${month+1}-$dayOfMonth"
            isDateSelected = true
        })
        binding.tvNext.setOnClickListener{
            if (validateCheckout()){
                proceedToBooking()
            }
        }
    }

    private fun setTicketSpinner(ticketList: ArrayList<CarnivalTickets>) {
        ticketList.add(CarnivalTickets(ticketList[0].id,ticketList[0].name,ticketList[0].price))
        val dataAdapter = TicketAdapter(ticketList, requireContext())
        binding.spTicket.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != ticketList.size - 1) {
                    binding.tvTicket.text = ticketList[p2].name
                    tickedId = ticketList[p2].id
                    isTicektSelected = true
                }
            }
        }
        binding.spTicket.adapter = dataAdapter
        binding.spTicket.setSelection(ticketList.size - 1)
    }

    private fun setKhashtaSpinner(khashtaList: ArrayList<CarnivalKhashtaSize>) {
        khashtaList.add(CarnivalKhashtaSize(khashtaList[0].id,khashtaList[0].size,khashtaList[0].carnivalTicketId,khashtaList[0].stock,khashtaList[0].price,khashtaList[0].days))
        val dataAdapter = KhashtaAdapter(khashtaList, requireContext())
        binding.spKhashta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != khashtaList.size - 1) {
                    binding.tvKhasta.text = khashtaList[p2].size
                    khashtaSizeId = khashtaList[p2].id
                    ticketPrice = khashtaList[p2].price
                    isKhashtaSizeselected = true
                    setUpCalendar()
                }
            }
        }
        binding.spKhashta.adapter = dataAdapter
        binding.spKhashta.setSelection(khashtaList.size - 1)
    }

    private fun validateCheckout(): Boolean {
        binding.apply {
            if (!isTicektSelected){
                requireActivity().setSnackbar(tvNext, getString(R.string.ticket_select_validation), true)
                return false
            }
            if (!isKhashtaSizeselected){
                requireActivity().setSnackbar(tvNext, getString(R.string.khashta_size_select_validation), true)
                return false
            }
            if (!isDateSelected){
                requireActivity().setSnackbar(tvNext, getString(R.string.date_select_validation), true)
                return false
            }
            if (customerField.isEmpty()){
                requireActivity().setSnackbar(tvNext, getString(R.string.customer_name_validation), true)
                return false
            }
            if (emailField.isEmpty()){
                requireActivity().setSnackbar(tvNext, getString(R.string.validation_empty_email), true)
                return false
            }
            if (!emailField.isEmailValid()){
                requireActivity().setSnackbar(tvNext, getString(R.string.validation_invalid_email), true)
                return false
            }
            if (mobileField.isEmpty()){
                requireActivity().setSnackbar(tvNext, getString(R.string.validation_empty_phone), true)
                return false
            }
            if (!mobileField.isPhoneValid()){
                requireActivity().setSnackbar(tvNext, getString(R.string.validation_invalid_phone), true)
                return false
            }
            if (agreeRadio.isSelected){
                requireActivity().setSnackbar(tvNext, getString(R.string.radio_agree_validation), true)
                return false
            }
        }
        return true
    }

    private fun proceedToBooking(){
        binding.apply {
            val addEvent =  AddEvent(carnivalId,carnivalTitle,tickedId,ticketPrice,khashtaSizeId, tvKhasta.text.toString(), dateToSend,emailField.text.toString(),customerField.text.toString(),mobileField.text.toString(),spclField.text.toString(),ticketPrice)
            val bundle = Bundle()
            bundle.putParcelable("eventDetail",addEvent)
            mainController.navigate(R.id.action_event_summary,bundle)
        }
    }

}