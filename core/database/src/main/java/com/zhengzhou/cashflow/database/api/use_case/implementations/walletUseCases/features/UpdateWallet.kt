package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.UpdateWalletInterface

class UpdateWallet(
    private val repository: RepositoryInterface
): UpdateWalletInterface {
    override suspend fun updateWallet(wallet: Wallet) {
        repository.updateWallet(wallet = wallet)
    }
}