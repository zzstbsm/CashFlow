package com.zhengzhou.cashflow.database.data.data_source.dao

import androidx.room.Dao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.BudgetPerCategoryDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.BudgetPerPeriodDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.CategoryDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.LocationDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.TagEntryDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.TagTransactionDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.TransactionDao
import com.zhengzhou.cashflow.database.data.data_source.dao.class_dao.WalletDao

@Dao
internal interface RepositoryDao : BudgetPerCategoryDao,
    BudgetPerPeriodDao,
    CategoryDao,
    LocationDao,
    TagEntryDao,
    TagTransactionDao,
    TransactionDao,
    WalletDao