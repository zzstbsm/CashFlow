package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletInterface
import java.util.UUID

class GetWallet(
    private val repository: RepositoryInterface
): GetWalletInterface {
    override suspend fun getWallet(walletUUID: UUID): Wallet? {
        return repository.getWallet(walletUUID = walletUUID)
    }
}