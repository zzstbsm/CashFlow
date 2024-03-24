package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Tag
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface GetTagListFromTransactionInterface {
    fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>>
}