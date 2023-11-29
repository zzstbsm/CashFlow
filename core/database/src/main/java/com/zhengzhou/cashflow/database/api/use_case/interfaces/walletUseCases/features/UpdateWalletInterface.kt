package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet

interface UpdateWalletInterface {
    suspend fun updateWallet(wallet: Wallet)
}