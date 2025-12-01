package com.app.meatz.data.network

import com.app.meatz.core.BaseRsm
import com.app.meatz.domain.remote.*
import com.app.meatz.domain.remote.address.Address
import com.app.meatz.domain.remote.address.AddressData
import com.app.meatz.domain.remote.area.Governorates
import com.app.meatz.domain.remote.box.Box
import com.app.meatz.domain.remote.generalResponse.ProductData
import com.app.meatz.domain.remote.myWallet.MyWallet
import com.app.meatz.domain.remote.orderDetails.OrderDetails
import com.app.meatz.domain.remote.productDetails.ProductDetails
import com.app.meatz.domain.remote.shopDetails.StoreDetails
import com.app.meatz.domain.remote.stores.Stores
import retrofit2.Response
import retrofit2.http.*

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
interface ApiService {

    @GET("ads")
    suspend fun getAds(): Response<BaseRsm<Ads>>

    @GET("home")
    suspend fun getHome(): Response<BaseRsm<Home>>

    @GET("our_boxes")
    suspend fun getOurBoxes(): Response<BaseRsm<Box>>

    @GET("product/{id}")
    suspend fun getBoxProductDetails(@Path("id") id: Int):
            Response<BaseRsm<BoxDetails>>

    @POST("add_to_cart")
    suspend fun addToCart(@Body hashMap: HashMap<String, Any>):
            Response<BaseRsm<Cart>>

    @GET("clear_cart")
    suspend fun deleteCart(): Response<BaseRsm<DeleteCart>>

    @GET("featured_products")
    suspend fun featuredProducts(): Response<BaseRsm<FeaturedProductsBase>>

    @GET("stores")
    suspend fun getStores(@Query("category_id") categoryId: Int): Response<BaseRsm<Stores>>

    @GET("stores/{storeId}")
    suspend fun getStoreDetails(@Path("storeId") storedId: Int,
                                @Query("category_id") categoryId: String,
                                @Query("filter_by") sort: String): Response<BaseRsm<StoreDetails>>

    @GET("featured_stores")
    suspend fun getFeaturesStores(@Query("sub_category_id") subCategoryID : String): Response<BaseRsm<Stores>>

    @GET("search")
    suspend fun search(@Query("keyword") keyword: String, @Query("more") more: Int):
            Response<BaseRsm<Search>>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(@Field("email") email: String, @Field("password") password: String):
            Response<BaseRsm<User>>

    @POST("social_login")
    @FormUrlEncoded
    suspend fun socialLogin(@FieldMap hashMap: HashMap<String, Any>)
            : Response<BaseRsm<User>>

    @POST("forget")
    @FormUrlEncoded
    suspend fun forgetPassword(@Field("email") email: String): Response<BaseRsm<ForgetPassword>>

    @POST("signup")
    @FormUrlEncoded
    suspend fun signup(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<User>>

    @GET("pages/{id}")
    suspend fun getPageDetails(@Path("id") pageid: Int): Response<BaseRsm<Page>>

    @POST("edit_profile")
    @FormUrlEncoded
    suspend fun editProfile(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<User>>

    @POST("change_password")
    @FormUrlEncoded
    suspend fun changePassword(@Field("old_password") oldPass: String, @Field("password") newPass: String)
            : Response<BaseRsm<Any>>

    @GET("likes")
    suspend fun getWishlist(@Query("page") page: Int): Response<BaseRsm<List<ProductData>>>

    @GET("product/{productId}/like")
    suspend fun likeDisLikeProduct(@Path("productId") prodcutId: Int): Response<BaseRsm<LikeDislike>>

    @GET("addresses")
    suspend fun getAddress(): Response<BaseRsm<List<Address>>>

    @DELETE("addresses/{addressId}")
    suspend fun deleteAddress(@Path("addressId") addressId: Int): Response<BaseRsm<List<Address>>>

    @GET("areas")
    suspend fun getAreas(): Response<BaseRsm<List<Governorates>>>

    @POST("addresses")
    @FormUrlEncoded
    suspend fun addAddress(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<Address>>

    @PUT("addresses/{id}/")
    suspend fun editAddress(@Path("id") addressId: Int, @Body address: AddressData)
            : Response<BaseRsm<Address>>

    @GET("orders")
    suspend fun getOrders(): Response<BaseRsm<List<Orders>>>

    @GET("orders/{orderId}")
    suspend fun getOrderDetails(@Path("orderId") orderid: Int): Response<BaseRsm<OrderDetails>>

    @GET("orders/{orderId}/cancel_request")
    suspend fun cancelRequest(@Path("orderId") orderId: Int): Response<BaseRsm<OrderDetails>>

    @GET("boxes")
    suspend fun getMyBoxes(): Response<BaseRsm<List<MyBox>>>

    @DELETE("boxes/{boxId}")
    suspend fun deleteBox(@Path("boxId") boxId: Int): Response<BaseRsm<List<MyBox>>>

    @GET("boxes/{boxId}/add_to_cart")
    suspend fun addBoxToMyCart(@Path("boxId") boxId: Int): Response<BaseRsm<Any>>

    @POST("boxes")
    @FormUrlEncoded
    suspend fun createBox(@Field("name") boxName: String): Response<BaseRsm<List<MyBox>>>

    @GET("boxes/{boxId}")
    suspend fun getMyBoxDetails(@Path("boxId") boxId: Int): Response<BaseRsm<MyBoxDetails>>

    @POST("boxes/{boxId}/remove_item")
    @FormUrlEncoded
    suspend fun removeProductFromBox(@Path("boxId") boxId: Int, @Field("product_id")
    productId: String): Response<BaseRsm<Any>>


    @GET("contacts")
    suspend fun getContacts(): Response<BaseRsm<Contacts>>

    @POST("contactus")
    @FormUrlEncoded
    suspend fun sendMessage(@FieldMap hashMap: HashMap<String, String>): Response<BaseRsm<Any>>

    @GET("notifications")
    suspend fun getNotifications(@Query("page") page: Int): Response<BaseRsm<List<Notifications>>>

    @GET("product/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: Int): Response<BaseRsm<ProductDetails>>

    @GET("cart")
    suspend fun getCart(): Response<BaseRsm<Cart>>

    @GET("cart")
    suspend fun clearOutStockItems(@Query("clear_out_of_stock") clear_out_of_stock: Int):
            Response<BaseRsm<Cart>>

    @POST("remove_from_cart")
    @FormUrlEncoded
    suspend fun removeProductFromCart(@Field("product_id") prodcutId: Int): Response<BaseRsm<Cart>>

    @POST("add_item_to_boxes")
    @FormUrlEncoded
    suspend fun addItemToBoxes(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<AddItemToBoxes>>

    @GET("boxes/{boxId}/clear_items")
    suspend fun deleteBoxItems(@Path("boxId") boxId: Int): Response<BaseRsm<Any>>

    @POST("check_copon")
    @FormUrlEncoded
    suspend fun applyCoupon(@Field("code") code: String, @Field("total") total: String)
            : Response<BaseRsm<Coupon>>

    @POST("new_order")
    @FormUrlEncoded
    suspend fun checkout(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<Checkout>>

    @GET("orders/{orderId}/reorder")
    suspend fun reorder(@Path("orderId") orderid: Int): Response<BaseRsm<Cart>>

    @GET("pages")
    suspend fun getPages(): Response<BaseRsm<List<Pages>>>

    @GET("special_boxes")
    suspend fun getOfferBoxes(@Query("keyword") keyword: String, @Query("category_id") categoryId: String): Response<BaseRsm<OfferBoxes>>

    @GET("special_boxes/{boxId}")
    suspend fun getOfferBoxDetails(@Path("boxId") boxId: Int): Response<BaseRsm<OfferBoxDetails>>

    @GET("profile")
    suspend fun getUserProfile(): Response<BaseRsm<User>>

    @GET("wallet_cards")
    suspend fun getMyWallet(): Response<BaseRsm<MyWallet>>

    @POST("wallet_charge")
    @FormUrlEncoded
    suspend fun chargeWalletWithAmount(@Field("amount") amount: String): Response<BaseRsm<ChargeWallet>>

    @POST("wallet_charge")
    @FormUrlEncoded
    suspend fun chargeWalletWithCardId(@Field("card_id") cardId: Int): Response<BaseRsm<ChargeWallet>>

    @GET("new_order")
    suspend fun getCheckoutDetails(): Response<BaseRsm<CheckoutDetails>>

    @GET("logout")
    suspend fun logout(): Response<BaseRsm<Any>>

    @GET("delete-account")
    suspend fun deleteAccount(): Response<BaseRsm<Any>>

    @GET("cart-count")
    suspend fun cartCount(): Response<BaseRsm<Any>>

    @GET("carnival/{slider_id}")
    suspend fun getCarnivalDetail(@Path("slider_id") sliderId: Int): Response<BaseRsm<Carnival>>

    @GET("carnivals/ticket_types")
    suspend fun getCarnivalTickets(): Response<BaseRsm<List<CarnivalTickets>>>

    @GET("carnivals/khashta_sizes")
    suspend fun getCarnivalKhashtaSizes(): Response<BaseRsm<List<CarnivalKhashtaSize>>>

    @POST("carnivals/booking")
    @FormUrlEncoded
    suspend fun carnivalBooking(@FieldMap hashMap: HashMap<String, Any>): Response<BaseRsm<CarnivalBooking>>

}