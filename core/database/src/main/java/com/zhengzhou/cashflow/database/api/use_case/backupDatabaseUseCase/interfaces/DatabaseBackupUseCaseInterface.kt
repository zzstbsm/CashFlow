package com.zhengzhou.cashflow.database.api.use_case.backupDatabaseUseCase.interfaces

import com.zhengzhou.cashflow.data.databaseBackup.DatabaseBackup

interface DatabaseBackupUseCaseInterface {

    /**
     * Do a backup of the database
     */
    suspend fun getDatabaseBackup(): DatabaseBackup

    /**
     * Restore a backup of the database
     */
    suspend fun restoreDatabaseBackup()
}