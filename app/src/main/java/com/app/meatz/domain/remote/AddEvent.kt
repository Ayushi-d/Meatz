package com.app.meatz.domain.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEvent(
    val carnivalId: Int = 0,
    val carnivalTitle: String = "",
    val ticketID: Int = 0,
    val ticketPrice: String = "",
    val khashtaSizeId: Int = 0,
    val khashtaType: String = "",
    val date: String = "",
    val email: String = "",
    val name: String = "",
    val mobile: String = "",
    val specailRequest: String = "",
    val price: String = ""

): Parcelable

