package com.app.meatz.data.application

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import com.app.meatz.data.utils.LocaleHelper.locale
import java.util.*

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

const val REQUEST_FLEXIBLE_UPDATE = 100

fun getContext(): Context {
  return App.instance.applicationContext
}

fun getStringByLocal(id: Int): String {
    val configuration = Configuration(getContext().resources.configuration)
    configuration.setLocale(Locale(locale))
    return getContext().createConfigurationContext(configuration).resources.getString(id)
}

fun getStringByLocal(@StringRes resId: Int, vararg formatArgs: Any): String {
    val configuration = Configuration(getContext().resources.configuration)
    configuration.setLocale(Locale(locale))
    return getContext().createConfigurationContext(configuration).resources.getString(resId, *formatArgs)
}

const val DISABLE_SELECTED_VIEW_OPTIONS = false


const val BANNER_TYPE = "BannerType"
const val ITEM_TYPE = "ItemType"

const val BANNER_PRODUCT = "product"
const val BANNER_STORE = "store"
const val BANNER_BOX = "box"

const val BOX_ID = "BoxId"
const val ADS_URL = "AdsUrl"

const val SELECTED_CATEGORY = "SelectedCategory"
const val MEAT_CATEGORY = 1
const val POULTRY_CATEGORY = 2
const val FISH_CATEGORY = 3

const val SHOP_ID = "ShopId"

const val ITEM_BOX_TYPE = "box"
const val ITEM_PRODUCT_TYPE = "product"
const val ITEM_SPECIAL_BOX = "special_box"


const val SEARCH_KEY_WORD = "SearchKeyWord"
const val SEARCH_ITEMS_TAB_IS_SELECTED = "ItemsIsSelected"

const val PAGE_ITEM = "PageItem"
const val FROM_REGISTER = "FromRegister"

const val ADDRESS_OBJ = "AddressObj"
const val ORDER_ID = "order_id"

const val MEATZ_WHATSAPP = "MeatzWhatsapp"

const val PRODUCT_ID = "ProductId"
const val SUBTOTAL = "SubTotal"

const val CHECKOUT_STATUS_OBJ = "CheckoutStatus"
const val PAYMENT_URL = "PaymentUrl"
