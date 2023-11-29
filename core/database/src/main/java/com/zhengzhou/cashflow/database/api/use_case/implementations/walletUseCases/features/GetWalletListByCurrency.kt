package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListByCurrencyInterface
import kotlinx.coroutines.flow.Flow

class GetWalletListByCurrency(
    private val repository: RepositoryInterface
): GetWalletListByCurrencyInterface {
    override fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>> {
        return repository.getWalletListByCurrency(currency = currency)
    }
}