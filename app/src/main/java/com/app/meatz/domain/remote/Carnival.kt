package com.app.meatz.domain.remote
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carnival(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("hours")
    val hours: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("contact_us")
    val contactUs: String,
    @SerializedName("slider")
    val slider: Slider,
    @SerializedName("partners")
    val partners: List<Partners>,
    @SerializedName("carnival_khashta_sizes")
    val carnivalKhashtaSizes: List<CarnivalKhashtaSize>
): Parcelable

@Parcelize
data class Partners(
    @SerializedName("id")
    val id: Int,
    @SerializedName("carnival_id")
    val carnivalID: Int,
    @SerializedName("logo")
    val logo: String,
): Parcelable

data class CarnivalTickets(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
)

@Parcelize
data class CarnivalKhashtaSize(
    @SerializedName("id")
    val id: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("carnival_ticket_type_id")
    val carnivalTicketId: Int,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("days")
    val days:  List<CarnivalDays>,

):Parcelable

data class CarnivalBooking(
    @SerializedName("booking_id")
    val bookingId: Int,
    @SerializedName("paymentUrl")
    val paymentUrl: String,
)

@Parcelize

data class CarnivalDays(
    @SerializedName("id")
    val id: Int,
    @SerializedName("carnival_khashta_size_id")
    val carnivalKhashtaSizeId: Int,
    @SerializedName("total_booked")
    val totalBooked: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("is_day_off")
    val isDayOff: String,
    @SerializedName("is_bookings_full")
    val isBookingFull: String,
): Parcelable
