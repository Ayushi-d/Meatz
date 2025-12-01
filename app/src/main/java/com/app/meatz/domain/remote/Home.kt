package com.app.meatz.domain.remote


import android.os.Parcelable
import com.app.meatz.domain.remote.shopDetails.StoreSubCategories
import com.app.meatz.domain.remote.stores.SubCategories
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Home(
        @SerializedName("boxes")
        val boxes: List<Boxes>,
        @SerializedName("categories")
        val categories: List<Category>,
        @SerializedName("featured")
        val featured: List<Featured>,
        @SerializedName("sliders")
        val sliders: List<Slider>,
        @SerializedName("featured_products")
        val featuredProducts: List<FeaturedProducts>
)

@Parcelize
data class Category(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        var isSelected: Boolean = false
) : Parcelable

data class Featured(
        @SerializedName("color")
        val color: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("logo")
        val logo: String,
        @SerializedName("name")
        val name: String
)

data class FeaturedProductsBase(
        @SerializedName("featured_products")
        val featuredProducts: List<FeaturedProducts>,
)

data class FeaturedProducts(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_before")
        val price_before: String

)

data class Boxes(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String? = "",
        @SerializedName("name")
        val name: String? = "",
        @SerializedName("persons")
        val persons: Int? = 0,
        @SerializedName("price")
        val price: String? = "",
        @SerializedName("price_before")
        val priceBefore: Double = 0.0
)

@kotlinx.parcelize.Parcelize

data class Slider(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String? = "",
        @SerializedName("model")
        val model: String? = "",
        @SerializedName("model_id")
        val modelId: Int? = 0,
        @SerializedName("sort")
        val sort: Int? = 0,
        @SerializedName("status")
        val status: Int? = 0,
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("type")
        val type: String? = ""
): Parcelable