package com.app.meatz.presentation.featureAddress

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData
import com.app.meatz.domain.remote.address.AddressData

class AddressViewModel : ViewModel() {
    fun getAddress() = loadData { RestClient.api.getAddress() }
    fun deleteAddress(addressId: Int) = loadData { RestClient.api.deleteAddress(addressId) }
    fun getAreas() = loadData { RestClient.api.getAreas() }
    fun addAddress(hashMap: HashMap<String, Any>) = loadData { RestClient.api.addAddress(hashMap) }
    fun editAddress(addressObj: AddressData, addressId: Int) = loadData { RestClient.api.editAddress(addressId, addressObj) }
}