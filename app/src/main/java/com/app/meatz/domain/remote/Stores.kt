package com.app.meatz.domain.remote.stores


import com.app.meatz.domain.remote.generalResponse.Cart
import com.app.meatz.domain.remote.generalResponse.StoreData
import com.google.gson.annotations.SerializedName

data class Stores(
        @SerializedName("ads")
        val ads: List<Ad>,
        @SerializedName("stores")
        val storeData: List<StoreData>,
        @SerializedName("cart")
        val cart: Cart,
//        @SerializedName("categories")
//        val categories : List<StoreCategories>
)


data class Ad(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("model")
        val model: String,
        @SerializedName("model_id")
        val modelId: Int,
        @SerializedName("sort")
        val sort: Int,
        @SerializedName("status")
        val status: Int,
        @SerializedName("title")
        val title: String
)

data class StoreCategories(
        @SerializedName("id")
        val id: Int,
        @SerializedName("parent_id")
        val parentID: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("subcategories")
        val subcategories: List<SubCategories>,
)

data class SubCategories(
        @SerializedName("id")
        val id: Int,
        @SerializedName("subcategory_name")
        val name: Subcategoryname,
        @SerializedName("image")
        val image: String,
)

data class Subcategoryname(
        @SerializedName("ar")
        val ar: String,
        @SerializedName("en")
        val en: String,
)

