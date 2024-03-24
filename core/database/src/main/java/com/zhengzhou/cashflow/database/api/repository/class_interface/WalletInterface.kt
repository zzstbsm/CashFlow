package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WalletInterface {
    suspend fun getWallet(walletUUID: UUID): Wallet?
    suspend fun addWallet(wallet: Wallet)
    suspend fun deleteWallet(wallet: Wallet)
    suspend fun updateWallet(wallet: Wallet)
    fun getWalletCurrencyList(): Flow<List<Currency>>
    fun getWalletList(): Flow<List<Wallet>>
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>>
    fun getListOfNamesOfWallets(): Flow<List<String>>
    suspend fun getLastAccessedWallet(): Wallet?

}