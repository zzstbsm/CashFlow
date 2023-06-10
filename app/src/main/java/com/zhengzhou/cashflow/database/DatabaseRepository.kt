package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Room
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import java.util.*

private const val DATABASE_NAME = "Registry_DB"

class DatabaseRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope,
){

    companion object {

        private var INSTANCE: DatabaseRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseRepository(context)
            }
        }

        fun get(): DatabaseRepository {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseRepository must be initialized")
        }

    }

    private val database: RegisterDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            RegisterDatabase::class.java,
            DATABASE_NAME
        )
        //.addMigrations(migration_1_2, migration_2_3)
        .addMigrations(MIGRATION_1_2)
        .build()


    /* Category section */
    suspend fun getCategory(categoryUUID: UUID) : Category?
        = database.databaseDao().getCategory(categoryUUID = categoryUUID)
    suspend fun addCategory(category: Category) {
        database.databaseDao().addCategory(category)
    }
    suspend fun updateCategory(category: Category) {
        database.databaseDao().updateCategory(category)
    }
    suspend fun deleteCategory(category: Category) {
        database.databaseDao().deleteCategory(category)
    }
    fun getCategoryList(): Flow<List<Category>> = database.databaseDao().getCategoryList()

    /* Tag section */
    fun getTag(id: UUID): Tag? = database.databaseDao().getTag(id)
    suspend fun addTag(tag: Tag) {
        database.databaseDao().addTag(tag)
    }
    suspend fun updateTag(tag: Tag) {
        database.databaseDao().updateTag(tag)
    }
    suspend fun deleteTag(tag: Tag) {
        database.databaseDao().deleteTag(tag)
    }
    fun getTagList(): Flow<List<Tag>> = database.databaseDao().getTagList()

    /* Transaction section */
    suspend fun getTransaction(transactionId: UUID): Transaction? = database.databaseDao().getTransaction(transactionId)
    suspend fun addTransaction(transaction: Transaction) {
        database.databaseDao().addTransaction(transaction)
    }
    suspend fun updateTransaction(transaction: Transaction) {
        database.databaseDao().updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction) {
        database.databaseDao().deleteTransaction(transaction)
    }
    fun getTransactionListInWallet(idWallet: UUID): Flow<List<Transaction>>
            = database.databaseDao().getTransactionListInWallet(idWallet)

    /* Wallet section */
    suspend fun getWallet(walletUUID: UUID): Wallet? = database.databaseDao().getWallet(walletUUID)
    suspend fun addWallet(wallet: Wallet) {
        database.databaseDao().addWallet(wallet)
    }
    suspend fun deleteWallet(wallet: Wallet) {
        database.databaseDao().deleteWallet(wallet)
    }
    suspend fun updateWallet(wallet: Wallet) {
        database.databaseDao().updateWallet(wallet)
    }
    fun getWalletList(): Flow<List<Wallet>> = database.databaseDao().getWalletList()
    fun getWalletLastAccessed(): Wallet? {
        return database.databaseDao().getWalletLastAccessed()
    }
}