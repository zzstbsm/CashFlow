package com.zhengzhou.cashflow.ui.walletEdit

data class WalletEditUiState(
    val isLoading: Boolean = true,



    val amountOnScreen: String = "",
    val isErrorAmountOnScreen: Boolean = false,

    val isErrorWalletNameInUse: Boolean = false,
    val isErrorWalletNameNotValid: Boolean = false,
)