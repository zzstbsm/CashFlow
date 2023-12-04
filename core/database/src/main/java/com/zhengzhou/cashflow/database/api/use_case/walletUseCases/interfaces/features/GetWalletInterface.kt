package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface GetWalletInterface{
    suspend fun getLastAccessedWallet(): Wallet?
    fun getListOfNames(): Flow<List<String>>
    fun getUsedCurrenciesInAllWallets(): Flow<List<Currency>>
    fun getUsedCurrenciesInWallet(wallet: Wallet): Flow<List<Currency>>
    suspend fun getWallet(walletUUID: UUID): Wallet?
    fun getWalletList(): Flow<List<Wallet>>
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>>
}