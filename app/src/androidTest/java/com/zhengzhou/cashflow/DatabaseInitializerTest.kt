package com.zhengzhou.cashflow

import android.content.Context
import androidx.room.Room
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.database.RegisterDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun databaseRepositoryInitializerTest(
    context: Context,
): DatabaseRepositoryTest {

    val database = Room.inMemoryDatabaseBuilder(
        context,
        RegisterDatabase::class.java
    ).build()
    DatabaseRepositoryTest.initialize(database)
    return DatabaseRepositoryTest.get()
}

class DatabaseRepositoryTest(
    registerDatabase: RegisterDatabase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
): DatabaseRepository(registerDatabase) {

    companion object {

        private var INSTANCE: DatabaseRepositoryTest? = null

        fun initialize(registerDatabase: RegisterDatabase) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseRepositoryTest(registerDatabase)
            }
        }

        fun get(): DatabaseRepositoryTest {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseRepository must be initialized")
        }

    }

    //private val database = registerDatabase

    fun close() {
        val database = getDatabase()
        database.close()
    }
}