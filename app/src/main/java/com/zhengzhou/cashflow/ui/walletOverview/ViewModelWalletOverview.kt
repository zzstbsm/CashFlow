package com.zhengzhou.cashflow.ui.walletOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

data class WalletOverviewUiState(
    val wallet: Wallet = Wallet(),
    val walletList: List<Wallet> = listOf(),
    val ifZeroWallet: Boolean = false,
    val isLoading: Boolean = true,
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

            viewModelScope.launch {
                repository.getWalletList().collect {
                    _uiState.value = uiState.value.copy(
                        walletList = it,
                        ifZeroWallet = it.isEmpty(),
                        isLoading = false
                    )
                }
            }

            viewModelScope.launch {
                while (uiState.value.isLoading) {
                    delay(20)
                }
                if (!uiState.value.ifZeroWallet) {
                    loadLastAccessed()
                }
            }
        }
    }

    private suspend fun loadLastAccessed() {
        _uiState.value = uiState.value.copy(
            wallet = repository.getWalletLastAccessed() ?: Wallet.emptyWallet()
        )
    }

    fun deleteShownWallet() {
        viewModelScope.launch {
            repository.deleteWallet(uiState.value.wallet)
            _uiState.value = uiState.value.copy(
                wallet = Wallet()
            )
            loadLastAccessed()
            if (uiState.value.wallet.id == UUID(0L,0L)) {
                _uiState.value = uiState.value.copy(
                    ifZeroWallet = true
                )
            }
        }
    }

}