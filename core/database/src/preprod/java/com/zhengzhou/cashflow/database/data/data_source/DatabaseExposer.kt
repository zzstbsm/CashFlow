package com.zhengzhou.cashflow.database.data.data_source

import android.content.Context
import androidx.room.Room
import com.zhengzhou.cashflow.database.api.DatabaseInstance
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.data.repository.RepositoryImplementation

internal class DatabaseExposer {
    companion object {
        fun buildDatabase(
            context: Context,
        ) : RegisterDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RegisterDatabase::class.java,
                RegisterDatabase.DATABASE_NAME,
            ).build()
        }

        fun provideRepositoryImplementation(): RepositoryInterface {
            return RepositoryImplementation(
                DatabaseInstance.getDatabase().databaseDao()
            )
        }
    }
}