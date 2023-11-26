package com.zhengzhou.cashflow.database

import android.content.Context
import com.zhengzhou.cashflow.database.databaseRepositoryComponents.CategoryInterface
import com.zhengzhou.cashflow.database.databaseRepositoryComponents.TagEntryInterface
import com.zhengzhou.cashflow.database.databaseRepositoryComponents.TagInterface
import com.zhengzhou.cashflow.database.databaseRepositoryComponents.TransactionInterface
import com.zhengzhou.cashflow.database.databaseRepositoryComponents.WalletInterface

class DatabaseRepository private constructor() : CategoryInterface,
    TagInterface,
    TagEntryInterface,
    TransactionInterface,
    WalletInterface
{

    companion object {

        private var INSTANCE: DatabaseRepository? = null

        fun initialize(context: Context) {

            DatabaseInstance.initialize(context = context)

            if (INSTANCE == null) {
                INSTANCE = DatabaseRepository()
            }
        }

        fun get(): DatabaseRepository {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseRepository must be initialized")
        }

    }
}