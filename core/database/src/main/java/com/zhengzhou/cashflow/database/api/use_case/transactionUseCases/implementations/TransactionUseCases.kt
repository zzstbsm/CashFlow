package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features.AddTransaction
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features.DeleteTransaction
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features.GetTransaction
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features.UpdateTransaction
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.TransactionUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.AddTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.DeleteTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.GetTransactionInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.UpdateTransactionInterface

class TransactionUseCases(
    val repository: RepositoryInterface
) : TransactionUseCasesInterface,
        AddTransactionInterface by AddTransaction(repository),
        DeleteTransactionInterface by DeleteTransaction(repository),
        GetTransactionInterface by GetTransaction(repository),
        UpdateTransactionInterface by UpdateTransaction(repository)