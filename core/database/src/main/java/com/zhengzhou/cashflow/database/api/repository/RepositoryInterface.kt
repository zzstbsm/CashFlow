package com.zhengzhou.cashflow.database.api.repository

import com.zhengzhou.cashflow.database.api.repository.class_interface.CategoryInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagEntryInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagTransactionInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TransactionInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.WalletInterface

interface RepositoryInterface : CategoryInterface,
        TagEntryInterface,
        TagTransactionInterface,
        TransactionInterface,
        WalletInterface