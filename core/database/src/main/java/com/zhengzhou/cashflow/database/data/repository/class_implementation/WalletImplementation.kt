package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.class_interface.WalletInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal class WalletImplementation(
    private val dao: RepositoryDao
) : WalletInterface {
    override suspend fun getWallet(walletUUID: UUID): Wallet? {
        return dao.getWallet(walletUUID)
    }
    override suspend fun addWallet(wallet: Wallet) {
        dao.addWallet(wallet)
    }
    override suspend fun deleteWallet(wallet: Wallet) {
        dao.deleteWallet(wallet)
    }
    override suspend fun updateWallet(wallet: Wallet) {
        dao.updateWallet(wallet)
    }
    override fun getWalletCurrencyList(): Flow<List<Currency>> {
        return dao.getWalletCurrencyList()
    }
    override fun getWalletList(): Flow<List<Wallet>> {
        return dao.getWalletList()
    }
    override fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>> {
        return dao.getWalletListByCurrency(currency = currency)
    }
    override fun getListOfNamesOfWallets(): Flow<List<String>> {
        return dao.getWalletListOfNames()
    }
    override suspend fun getLastAccessedWallet(): Wallet? {
        return dao.getWalletLastAccessed()
    }
}