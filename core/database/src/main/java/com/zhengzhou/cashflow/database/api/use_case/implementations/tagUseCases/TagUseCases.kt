package com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases.features.AddTag
import com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases.features.GetTagEntryList
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.TagUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.AddTagInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.GetTagEntryListInterface

class TagUseCases(
    val repository: RepositoryInterface
): TagUseCasesInterface,
        AddTagInterface by AddTag(repository),
        GetTagEntryListInterface by GetTagEntryList(repository)
