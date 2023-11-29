package com.zhengzhou.cashflow.database.data.repository

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.CategoryInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagEntryInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.TransactionInterface
import com.zhengzhou.cashflow.database.api.repository.class_interface.WalletInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import com.zhengzhou.cashflow.database.data.repository.class_implementation.CategoryImplementation
import com.zhengzhou.cashflow.database.data.repository.class_implementation.TagEntryImplementation
import com.zhengzhou.cashflow.database.data.repository.class_implementation.TagImplementation
import com.zhengzhou.cashflow.database.data.repository.class_implementation.TransactionImplementation
import com.zhengzhou.cashflow.database.data.repository.class_implementation.WalletImplementation

internal class RepositoryImplementation(
    dao: RepositoryDao,
) : RepositoryInterface,
    CategoryInterface by CategoryImplementation(dao),
    TagEntryInterface by TagEntryImplementation(dao),
    TagInterface by TagImplementation(dao),
    TransactionInterface by TransactionImplementation(dao),
    WalletInterface by WalletImplementation(dao)