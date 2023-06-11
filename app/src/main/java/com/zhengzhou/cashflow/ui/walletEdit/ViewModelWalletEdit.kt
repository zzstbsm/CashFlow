package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*


data class WalletEditUiState(
    val wallet: Wallet = Wallet.emptyWallet(),
) {
    fun updateWalletName(
        name : String,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                name = name
            )
        )
    }

    fun updateWalletAmount(
        amount: Float,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                startAmount = amount
            )
        )
    }
}

enum class WalletEditOption(){
    ADD(),
    EDIT(),
}

class WalletEditViewModel(
    navController: NavController,
): ViewModel() {

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {
        viewModelScope.launch(Dispatchers.IO){



        }
    }

    fun updateWalletName(name: String) {
        _uiState.value = uiState.value.updateWalletName(name)
    }

    fun updateWalletAmount(amount: Float) {
        _uiState.value = uiState.value.updateWalletAmount(amount)
    }


}