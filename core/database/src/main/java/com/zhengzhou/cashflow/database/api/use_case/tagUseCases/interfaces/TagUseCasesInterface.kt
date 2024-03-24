package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces

import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.AddTagInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.DeleteTagInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagEntryListInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagListFromTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.UpdateTagInterface

interface TagUseCasesInterface: AddTagInterface,
        DeleteTagInterface,
        GetTagEntryListInterface,
        GetTagListFromTransactionInterface,
        UpdateTagInterface