package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.AddNewWalletInterface
import java.util.UUID

class AddNewWallet(
    private val repository: RepositoryInterface
): AddNewWalletInterface {
    override suspend fun addNewWallet(wallet: Wallet): Wallet {
        val initializedWallet = wallet.copy(id = UUID.randomUUID())
        repository.addWallet(initializedWallet)
        return initializedWallet
    }
}