package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetLastAccessedWalletInterface

class GetLastAccessedWallet(
    private val repository: RepositoryInterface
): GetLastAccessedWalletInterface {
    override suspend fun getLastAccessedWallet(): Wallet? {
        return repository.getLastAccessedWallet()
    }
}