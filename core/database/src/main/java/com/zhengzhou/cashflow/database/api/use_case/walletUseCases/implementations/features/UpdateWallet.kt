package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.UpdateWalletInterface

class UpdateWallet(
    private val repository: RepositoryInterface
): UpdateWalletInterface {
    override suspend fun updateWallet(wallet: Wallet) {
        repository.updateWallet(wallet = wallet)
    }
}