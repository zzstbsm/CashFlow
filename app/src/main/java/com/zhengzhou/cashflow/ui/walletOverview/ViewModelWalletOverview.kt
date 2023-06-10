package com.zhengzhou.cashflow.ui.walletOverview

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WalletOverviewUiState(
    val wallet: Wallet = Wallet(),
)

class WalletOverviewViewModel(
    navController: NavController,
): ViewModel() {

    var _uiState = MutableStateFlow(WalletOverviewUiState())
    val uiState: StateFlow<WalletOverviewUiState> = _uiState.asStateFlow()

    init {

    }
}