package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet

interface DeleteWalletInterface{
    suspend fun deleteWallet(wallet: Wallet)
}