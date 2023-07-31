package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Room
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Location
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.*

private const val DATABASE_NAME = "Registry_DB"

fun databaseRepositoryInitializer(
    context: Context,
): RegisterDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        RegisterDatabase::class.java,
        DATABASE_NAME
    )
    //.addMigrations(migration_1_2, migration_2_3)
    //.addMigrations(MIGRATION_1_2)
    .build()
}

open class DatabaseRepository(
    registerDatabase: RegisterDatabase,
    private val coroutineScope: CoroutineScope = GlobalScope,
){

    companion object {

        private var INSTANCE: DatabaseRepository? = null

        fun initialize(registerDatabase: RegisterDatabase) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseRepository(registerDatabase)
            }
        }

        fun get(): DatabaseRepository {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseRepository must be initialized")
        }

    }

    private val database = registerDatabase

    fun getDatabase(): RegisterDatabase = database

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
        database.databaseDao().addBudgetPeriod(budgetPeriod.copy(id = UUID.randomUUID()))
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
        database.databaseDao().addCategory(category.copy(id = UUID.randomUUID()))
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
    suspend fun getCategoryOccurrences(category: Category)
        = database.databaseDao().getCategoryOccurrences(categoryUUID = category.id)

    /* Location section */
    suspend fun getLocation(locationUUID: UUID): Location? = database.databaseDao().getLocation(locationUUID)
    suspend fun addLocation(location: Location) {
        database.databaseDao().addLocation(location)
    }
    suspend fun updateLocation(location: Location) {
        database.databaseDao().updateLocation(location)
    }
    suspend fun deleteLocation(location: Location) {
        database.databaseDao().deleteLocation(location)
    }

    /* Tag section */
    suspend fun getTag(tagUUID: UUID): Tag? {
        val tagTransaction = database.databaseDao().getTagTransaction(tagTransactionUUID = tagUUID)
            ?: return null
        val tagEntry = database.databaseDao().getTagEntry(tagTransaction.idTag)
        return Tag.merge(tagTransaction, tagEntry)
    }
    suspend fun addTag(tag: Tag): Tag {
        val initializedTag = tag.copy(
            id = UUID.randomUUID()
        )
        val (tagTransaction, tagEntry) = initializedTag.separate()
        when (initializedTag.count) {
            0 -> return initializedTag
            1 -> database.databaseDao().addTagEntry(tagEntry)
            else -> database.databaseDao().updateTagEntry(tagEntry)
        }
        database.databaseDao().addTagTransaction(tagTransaction)
        return initializedTag
    }
    suspend fun updateTag(tag: Tag) {

        val (tagTransaction, tagEntry) = tag.separate()

        if (tag.count == 0) {
            database.databaseDao().deleteTagEntry(tagEntry)
            database.databaseDao().deleteTagTransaction(tagTransaction)
        }

        database.databaseDao().updateTagTransaction(tagTransaction)
        database.databaseDao().updateTagEntry(tagEntry)
    }
    suspend fun deleteTag(tag: Tag) {
        val (tagTransaction, tagEntry) = tag.separate()
        database.databaseDao().deleteTagTransaction(tagTransaction)
        if (tagEntry.count > 0) {
            database.databaseDao().updateTagEntry(tagEntry)
        } else {
            database.databaseDao().deleteTagEntry(tagEntry)
        }
    }
    fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>> {
        return database.databaseDao().getTagTransactionFromTransaction(transactionUUID).map {
            it.mapNotNull {  tagTransaction ->
                val tagEntry: TagEntry? = database.databaseDao().getTagEntry(tagTransaction.idTag)
                Tag.merge(tagTransaction,tagEntry)
            }
        }
    }

    fun getTagEntryList(): Flow<List<TagEntry>> = database.databaseDao().getTagEntryList()
    /*
    /* TagTransaction section */
    suspend fun getTagTransaction(tagTransactionId: UUID): TagTransaction?
        = database.databaseDao().getTagTransaction(tagTransactionId)
    suspend fun addTagTransaction(tagTransaction: TagTransaction) {
        database.databaseDao().addTagTransaction(tagTransaction)
    }
    suspend fun updateTagTransaction(tagTransaction: TagTransaction) {
        database.databaseDao().updateTagTransaction(tagTransaction)
    }
    suspend fun deleteTagTransaction(tagTransaction: TagTransaction) {
        database.databaseDao().deleteTagTransaction(tagTransaction)
    }
    fun getTagTransactionFromTransaction(transactionUUID: UUID): Flow<List<TagTransaction>>
        = database.databaseDao().getTagTransactionFromTransaction(transactionUUID)

     */

    /* Transaction section */
    suspend fun getTransaction(transactionId: UUID): Transaction? = database.databaseDao().getTransaction(transactionId)
    suspend fun addTransaction(transaction: Transaction): Transaction {
        val initializedTransaction = transaction.copy(
            id = UUID.randomUUID()
        )
        database.databaseDao().addTransaction(initializedTransaction)
        return initializedTransaction
    }
    suspend fun updateTransaction(transaction: Transaction) {
        database.databaseDao().updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction) {
        database.databaseDao().deleteTransaction(transaction)
    }
    fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>> {
        val idList: List<UUID> = walletList.map { it.id }
        return when(idList.size) {
            0 -> flowOf(listOf())
            else -> database.databaseDao().getTransactionListInListOfWallet(idList)
        }

    }

    fun getTransactionListInWallet(idWallet: UUID): Flow<List<Transaction>>
        = database.databaseDao().getTransactionListInWallet(idWallet)
    fun getTransactionShortListInWallet(idWallet: UUID,numberOfEntries: Int): Flow<List<Transaction>>
        = database.databaseDao().getTransactionShortListInWallet(idWallet,numberOfEntries)
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>
        = database.databaseDao().getTransactionIsBlueprint()

    /* Wallet section */
    suspend fun getWallet(walletUUID: UUID): Wallet? = database.databaseDao().getWallet(walletUUID)
    suspend fun addWallet(wallet: Wallet) {

        database.databaseDao().addWallet(wallet.copy(id = UUID.randomUUID()))
    }
    suspend fun deleteWallet(wallet: Wallet) {
        database.databaseDao().deleteWallet(wallet)
    }
    suspend fun updateWallet(wallet: Wallet) {
        database.databaseDao().updateWallet(wallet)
    }
    fun getWalletCurrencyList(): Flow<List<Currency>>
        = database.databaseDao().getWalletCurrencyList()
    fun getWalletList(): Flow<List<Wallet>> = database.databaseDao().getWalletList()
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>>
        = database.databaseDao().getWalletListByCurrency(currency = currency)
    fun getWalletListOfNames(): Flow<List<String>> = database.databaseDao().getWalletListOfNames()
    suspend fun getWalletLastAccessed(): Wallet? {
        return database.databaseDao().getWalletLastAccessed()
    }
}