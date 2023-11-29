package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.DeleteWalletInterface

class DeleteWallet(
    private val repository: RepositoryInterface
): DeleteWalletInterface {
    override suspend fun deleteWallet(wallet: Wallet) {
        repository.deleteWallet(wallet = wallet)
    }
}