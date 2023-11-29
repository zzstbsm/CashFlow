package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow

interface GetWalletListInterface{
    fun getWalletList(): Flow<List<Wallet>>
}