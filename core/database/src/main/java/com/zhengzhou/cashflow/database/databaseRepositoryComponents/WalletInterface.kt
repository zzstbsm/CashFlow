package com.zhengzhou.cashflow.database.databaseRepositoryComponents

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseInstance
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface WalletInterface {
    suspend fun getWallet(walletUUID: UUID): Wallet? {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWallet(walletUUID)
    }
    suspend fun addWallet(wallet: Wallet): Wallet {

        val database = DatabaseInstance.get()

        val initializedWallet = wallet.copy(id = UUID.randomUUID())
        database.databaseDao().addWallet(initializedWallet)
        return initializedWallet
    }
    suspend fun deleteWallet(wallet: Wallet) {

        val database = DatabaseInstance.get()

        database.databaseDao().deleteWallet(wallet)
    }
    suspend fun updateWallet(wallet: Wallet) {

        val database = DatabaseInstance.get()

        database.databaseDao().updateWallet(wallet)
    }
    fun getWalletCurrencyList(): Flow<List<Currency>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWalletCurrencyList()
    }
    fun getWalletList(): Flow<List<Wallet>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWalletList()
    }
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWalletListByCurrency(currency = currency)
    }
    fun getWalletListOfNames(): Flow<List<String>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWalletListOfNames()
    }
    suspend fun getWalletLastAccessed(): Wallet? {

        val database = DatabaseInstance.get()

        return database.databaseDao().getWalletLastAccessed()
    }
}