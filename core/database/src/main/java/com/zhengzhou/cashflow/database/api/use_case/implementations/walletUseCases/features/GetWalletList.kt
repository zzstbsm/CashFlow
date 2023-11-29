package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListInterface
import kotlinx.coroutines.flow.Flow

class GetWalletList(
    private val repository: RepositoryInterface
): GetWalletListInterface {
    override fun getWalletList(): Flow<List<Wallet>> {
        return repository.getWalletList()
    }
}