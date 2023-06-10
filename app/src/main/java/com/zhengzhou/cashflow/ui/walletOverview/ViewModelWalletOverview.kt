package com.zhengzhou.cashflow.ui.walletOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class WalletOverviewUiState(
    val wallet: Wallet = Wallet(),
) {
    suspend fun updateWallet(wallet: Wallet): WalletOverviewUiState {
        val repository = DatabaseRepository.get()

        val tempWallet = wallet.copy(
            lastAccess = Date()
        )
        repository.updateWallet(wallet = tempWallet)
        return this.copy(
            wallet = tempWallet
        )
    }
}

class WalletOverviewViewModel(
    navController: NavController,
): ViewModel() {

    private var _uiState = MutableStateFlow(WalletOverviewUiState())
    val uiState: StateFlow<WalletOverviewUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {
        viewModelScope.launch(Dispatchers.IO){

            var lastAccessedWallet: Wallet? = null

            try {
                lastAccessedWallet = repository.getWalletLastAccessed()
            } catch (e: Exception) {
                EventMessages.sendMessage("No wallets")
            }

            if (lastAccessedWallet == null) {
                lastAccessedWallet = Wallet.emptyWallet()
            }
            _uiState.value = uiState.value.updateWallet(lastAccessedWallet)
        }
    }
}