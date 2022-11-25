package com.app.meatz.domain.remote.shopDetails


import com.app.meatz.domain.remote.Category
import com.app.meatz.domain.remote.generalResponse.Banner
import com.app.meatz.domain.remote.generalResponse.Cart
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.stores.SubCategories
import com.google.gson.annotations.SerializedName

data class StoreDetails(
        @SerializedName("ads")
        val banners: List<Banner>,
        @SerializedName("cart")
        val cart: Cart,
        @SerializedName("store")
        val store: Store,
        @SerializedName("categories")
        val categories: List<StoreCategory>,
)

data class StoreCategory(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("subcategories")
        val storeSubCategories: List<StoreSubCategories>,
        var isSelected: Boolean = false
)


data class Store(
        @SerializedName("color")
        val color: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("logo")
        val logo: String,
        @SerializedName("banner")
        val banner: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("cat_products")
        val products: List<CatProducts>,
        @SerializedName("products_count")
        val productsCount: Int
)

data class CatProducts(
        @SerializedName("products")
        val products: List<ProductData>,
        @SerializedName("subcategory")
        val subCatName: String,
)


data class StoreSubCategories(
        @SerializedName("id")
        val id: Int,
        @SerializedName("subcategory_name")
        val name: StoreSubcategoryname,
        @SerializedName("image")
        val image: String,
)

data class StoreSubcategoryname(
        @SerializedName("ar")
        val ar: String,
        @SerializedName("en")
        val en: String,
)
