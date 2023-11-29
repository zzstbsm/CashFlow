package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetUsedCurrenciesInterface
import kotlinx.coroutines.flow.Flow

class GetUsedCurrencies(
    private val repository: RepositoryInterface
): GetUsedCurrenciesInterface {
    override fun getUsedCurrencies(wallet: Wallet): Flow<List<Currency>> {
        return repository.getWalletCurrencyList()
    }
}