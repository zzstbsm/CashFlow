package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features.AddTag
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features.DeleteTag
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features.GetTagEntryList
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features.GetTagListFromTransaction
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features.UpdateTag
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.TagUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.AddTagInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.DeleteTagInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagEntryListInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagListFromTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.UpdateTagInterface

class TagUseCases(
    val repository: RepositoryInterface
) : TagUseCasesInterface,
        AddTagInterface by AddTag(repository),
        DeleteTagInterface by DeleteTag(repository),
        GetTagEntryListInterface by GetTagEntryList(repository),
        GetTagListFromTransactionInterface by GetTagListFromTransaction(repository),
        UpdateTagInterface by UpdateTag(repository)