package com.app.meatz.presentation.myWallet

import androidx.lifecycle.ViewModel
import com.app.meatz.data.network.RestClient
import com.app.meatz.data.network.loadData

class MyWalletViewModel : ViewModel() {
    fun getMyWallet() = loadData { RestClient.api.getMyWallet() }
    fun chargeWalletWithAmount(amount: String) = loadData { RestClient.api.chargeWalletWithAmount(amount) }
    fun chargeWalletWithCards(cardId: Int) = loadData { RestClient.api.chargeWalletWithCardId(cardId) }
}