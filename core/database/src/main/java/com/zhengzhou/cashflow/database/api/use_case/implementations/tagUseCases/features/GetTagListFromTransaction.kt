package com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.GetTagListFromTransactionInterface
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetTagListFromTransaction(
    val repository: RepositoryInterface
) : GetTagListFromTransactionInterface {
    override fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>> {
        return repository.getTagListFromTransaction(transactionUUID = transactionUUID)
    }
}