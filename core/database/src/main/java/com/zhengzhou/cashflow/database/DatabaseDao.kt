package com.zhengzhou.cashflow.database

import androidx.room.Dao
import com.zhengzhou.cashflow.database.databaseDaoComponents.BudgetPerCategoryDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.BudgetPerPeriodDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.CategoryDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.LocationDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.TagEntryDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.TagTransactionDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.TransactionDao
import com.zhengzhou.cashflow.database.databaseDaoComponents.WalletDao

@Dao
internal interface DatabaseDao : BudgetPerCategoryDao,
    BudgetPerPeriodDao,
    CategoryDao,
    LocationDao,
    TagEntryDao,
    TagTransactionDao,
    TransactionDao,
    WalletDao