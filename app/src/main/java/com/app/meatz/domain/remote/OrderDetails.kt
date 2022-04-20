package com.app.meatz.domain.remote.orderDetails


import com.app.meatz.domain.remote.address.Address
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class OrderDetails(
        @SerializedName("address")
        val address: Address,
        @SerializedName("can_reorder")
        val canReorder: Int,
        @SerializedName("can_cancel")
        val can_cancel: Int,
        @SerializedName("code")
        val code: String,
        @SerializedName("copon")
        val copon: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delivery")
        val delivery: Delivery,
        @SerializedName("id")
        val id: Int,
        @SerializedName("payment")
        val payment: Payment,
        @SerializedName("products")
        val products: List<OrderProducts>,
        @SerializedName("status")
        val status: String,
        @SerializedName("user")
        val user: User,
        @SerializedName("payment_method")
        val payment_method: String
)

data class Payment(
        @SerializedName("delivery")
        val delivery: String,
        @SerializedName("discount")
        val discount: String,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("subtotal")
        val subtotal: String,
        @SerializedName("total")
        val total: String,
        @SerializedName("payment_id")
        val payment_id: String?,
        @SerializedName("transaction_id")
        val transaction_id: String?
)

data class User(
        @SerializedName("id")
        val id: Int,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("username")
        val username: Any
)

data class Delivery(
        @SerializedName("date")
        val date: String?,
        @SerializedName("mode")
        val mode: String,
        @SerializedName("time")
        val time: String?,
        @SerializedName("delivery_type")
        val delivery_type: String
)

data class OrderProducts(
        @SerializedName("image")
        val image: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("count")
        val count: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("total")
        val total: String,
        @SerializedName("options")
        val options: List<Options>,
        @SerializedName("persons")
        val persons: Int = 0,
        @SerializedName("store")
        val store: StoreData?,
)

data class Options(
        @SerializedName("count")
        val count: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("option_id")
        val optionId: Any,
        @SerializedName("parent_name")
        val parentName: String,
        @SerializedName("price")
        val price: String
)