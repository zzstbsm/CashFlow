package com.zhengzhou.cashflow.database.api.use_case.backupDatabaseUseCase.implementations

import com.zhengzhou.cashflow.data.databaseBackup.DatabaseBackup
import com.zhengzhou.cashflow.database.api.DatabaseInstance
import com.zhengzhou.cashflow.database.api.use_case.backupDatabaseUseCase.interfaces.DatabaseBackupUseCaseInterface

class DatabaseBackupUseCase() : DatabaseBackupUseCaseInterface {

    override suspend fun getDatabaseBackup(): DatabaseBackup {
        val dao = DatabaseInstance.getDatabase().databaseDao()

        return DatabaseBackup(
            categoryList = dao.getAllCategories(),
            tagEntryList = dao.getAllTagEntry(),
            tagTransactionList = dao.getAllTagTransaction(),
            transactionList = dao.getAllTransaction(),
            walletList = dao.getAllWallet(),
        )

    }

    override suspend fun restoreDatabaseBackup() {
        TODO("Not yet implemented")
    }
}