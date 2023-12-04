package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.GetWalletInterface
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetWallet(
    private val repository: RepositoryInterface
): GetWalletInterface {
    override fun getUsedCurrenciesInWallet(wallet: Wallet): Flow<List<Currency>> {
        return repository.getWalletCurrencyList()
    }

    override fun getListOfNames(): Flow<List<String>> {
        return repository.getListOfNamesOfWallets()
    }

    override suspend fun getLastAccessedWallet(): Wallet? {
        return repository.getLastAccessedWallet()
    }

    override fun getWalletList(): Flow<List<Wallet>> {
        return repository.getWalletList()
    }

    override fun getUsedCurrenciesInAllWallets(): Flow<List<Currency>> {
        return repository.getWalletCurrencyList()
    }

    override fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>> {
        return repository.getWalletListByCurrency(currency = currency)
    }

    override suspend fun getWallet(walletUUID: UUID): Wallet? {
        return repository.getWallet(walletUUID = walletUUID)
    }
}