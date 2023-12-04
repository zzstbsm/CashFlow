package com.zhengzhou.cashflow.wallet_overview.view_model

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.wallet_overview.data_structure.TransactionAndCategory

internal data class WalletOverviewUiState(
    val wallet: Wallet = Wallet.loadingWallet(),
    val currentAmountInTheWallet: Float = 0f,

    val ifZeroWallet: Boolean = false,
    val walletList: List<Wallet> = listOf(),
    val transactionAndCategoryList: List<TransactionAndCategory> = listOf(),

    val isLoadingWallet: Boolean = true,
    val isLoadingTransactions: Boolean = false,

    val showSelectWallet: Boolean = false,
)
