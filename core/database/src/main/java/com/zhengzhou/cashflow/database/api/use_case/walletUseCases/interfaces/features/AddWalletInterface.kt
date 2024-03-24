package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Wallet

interface AddWalletInterface{
    suspend fun addWallet(wallet: Wallet): Wallet
}