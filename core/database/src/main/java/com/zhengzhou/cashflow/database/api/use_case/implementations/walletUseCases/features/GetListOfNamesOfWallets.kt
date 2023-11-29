package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetListOfNamesOfWalletsInterface
import kotlinx.coroutines.flow.Flow

class GetListOfNamesOfWallets(
    private val repository: RepositoryInterface
): GetListOfNamesOfWalletsInterface {
    override fun getListOfNames(): Flow<List<String>> {
        return repository.getListOfNamesOfWallets()
    }
}