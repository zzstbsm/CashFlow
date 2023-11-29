package com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases

import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.AddTagInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.DeleteTagInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.GetTagEntryListInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.GetTagListFromTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.UpdateTagInterface

interface TagUseCasesInterface: AddTagInterface,
        DeleteTagInterface,
        GetTagEntryListInterface,
        GetTagListFromTransactionInterface,
        UpdateTagInterface