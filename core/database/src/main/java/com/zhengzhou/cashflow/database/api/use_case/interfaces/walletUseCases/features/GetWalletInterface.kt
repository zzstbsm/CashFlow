package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import java.util.UUID

interface GetWalletInterface{
    suspend fun getWallet(walletUUID: UUID): Wallet?
}