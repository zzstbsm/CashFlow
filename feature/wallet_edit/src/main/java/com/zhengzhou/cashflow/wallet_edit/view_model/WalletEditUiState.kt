package com.zhengzhou.cashflow.wallet_edit.view_model

import com.zhengzhou.cashflow.data.Wallet

internal data class WalletEditUiState(
    val isLoading: Boolean = true,

    val wallet: Wallet = Wallet.newEmpty(),

    val amountOnScreen: String = "",
    val isErrorAmountOnScreen: Boolean = false,

    val isErrorWalletNameInUse: Boolean = false,
    val isErrorWalletNameNotValid: Boolean = false,
)