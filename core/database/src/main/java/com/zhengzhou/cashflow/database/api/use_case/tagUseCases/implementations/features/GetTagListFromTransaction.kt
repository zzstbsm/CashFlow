package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagListFromTransactionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class GetTagListFromTransaction(
    val repository: RepositoryInterface
) : GetTagListFromTransactionInterface {
    override fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>> {

        return repository.getTagTransactionListFromTransaction(transactionUUID).map {
            it.mapNotNull {  tagTransaction ->
                val tagEntry: TagEntry? = repository.getTagEntry(tagTransaction.tagUUID)
                Tag.merge(tagTransaction, tagEntry)
            }
        }
    }
}