package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.Update
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
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
        //.addMigrations(MIGRATION_1_2)
        .build()



    /* BudgetCategory Section */
    suspend fun getBudgetCategory(budgetPeriodUUID: UUID, categoryUUID: UUID): BudgetCategory?
        = database.databaseDao().getBudgetCategory(budgetPeriodUUID,categoryUUID)
    suspend fun addBudgetCategory(budgetCategory: BudgetCategory) {
        database.databaseDao().addBudgetCategory(budgetCategory)
    }
    suspend fun updateBudgetCategory(budgetCategory: BudgetCategory)  {
        database.databaseDao().updateBudgetCategory(budgetCategory)
    }
    suspend fun deleteBudgetCategory(budgetCategory: BudgetCategory) {
        database.databaseDao().deleteBudgetCategory(budgetCategory)
    }


    /* BudgetPeriod section */
    fun getBudgetPeriodListFromWallet(walletUUID: UUID): Flow<List<BudgetPeriod>>
        = database.databaseDao().getBudgetPeriodListFromWallet(walletUUID)
    suspend fun addBudgetPeriod(budgetPeriod: BudgetPeriod) {
        database.databaseDao().addBudgetPeriod(budgetPeriod)
    }
    suspend fun updateBudgetPeriod(budgetPeriod: BudgetPeriod) {
        database.databaseDao().updateBudgetPeriod(budgetPeriod)
    }
    suspend fun deleteBudgetPeriod(budgetPeriod: BudgetPeriod) {
        database.databaseDao().deleteBudgetPeriod(budgetPeriod)
    }
    fun getBudgetPeriodLastActive(walletUUID: UUID): BudgetPeriod?
        = database.databaseDao().getBudgetPeriodLastActive(walletUUID)

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
    fun getCategoryListByTransactionType(transactionType: TransactionType): Flow<List<Category>> {
        return database.databaseDao().getCategoryListByTransactionType(transactionTypeId = transactionType.id)
    }

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
    fun getTransactionShortListInWallet(idWallet: UUID,numberOfEntries: Int): Flow<List<Transaction>>
        = database.databaseDao().getTransactionShortListInWallet(idWallet,numberOfEntries)

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
    suspend fun getWalletLastAccessed(): Wallet? {
        return database.databaseDao().getWalletLastAccessed()
    }
}