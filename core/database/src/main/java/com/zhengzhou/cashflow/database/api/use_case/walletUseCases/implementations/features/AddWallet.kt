package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.AddWalletInterface
import java.util.UUID

class AddWallet(
    private val repository: RepositoryInterface
): AddWalletInterface {
    override suspend fun addWallet(wallet: Wallet): Wallet {
        val initializedWallet = wallet.copy(id = UUID.randomUUID())
        repository.addWallet(initializedWallet)
        return initializedWallet
    }
}