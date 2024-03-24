package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces

import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.AddTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.DeleteTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.GetTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.UpdateTransactionInterface

interface TransactionUseCasesInterface: AddTransactionInterface,
        DeleteTransactionInterface,
        GetTransactionInterface,
        UpdateTransactionInterface