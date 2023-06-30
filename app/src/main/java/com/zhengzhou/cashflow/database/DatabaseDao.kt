package com.zhengzhou.cashflow.database

import androidx.room.*
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DatabaseDao {

    // BudgetCategory Section
    @Query("SELECT * FROM budget_category WHERE (id_period=(:budgetPeriodUUID) AND id_category=(:categoryUUID))")
    suspend fun getBudgetCategory(budgetPeriodUUID: UUID, categoryUUID: UUID): BudgetCategory?
    @Insert(entity = BudgetCategory::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addBudgetCategory(budgetCategory: BudgetCategory)
    @Update(entity = BudgetCategory::class)
    suspend fun updateBudgetCategory(budgetCategory: BudgetCategory)
    @Delete(entity = BudgetCategory::class)
    suspend fun deleteBudgetCategory(budgetCategory: BudgetCategory)


    // BudgetPeriod section
    @Query("SELECT * FROM budget_period WHERE id_wallet=(:walletUUID)")
    fun getBudgetPeriodListFromWallet(walletUUID: UUID): Flow<List<BudgetPeriod>>
    @Insert(entity = BudgetPeriod::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addBudgetPeriod(budgetPeriod: BudgetPeriod)
    @Update(entity = BudgetPeriod::class)
    suspend fun updateBudgetPeriod(budgetPeriod: BudgetPeriod)
    @Delete(entity = BudgetPeriod::class)
    suspend fun deleteBudgetPeriod(budgetPeriod: BudgetPeriod)
    @Query("SELECT * FROM budget_period WHERE id_wallet=(:walletUUID) ORDER BY end_date DESC LIMIT 1")
    fun getBudgetPeriodLastActive(walletUUID: UUID): BudgetPeriod?

    // Category section
    @Query("SELECT * FROM category WHERE id=(:categoryUUID)")
    suspend fun getCategory(categoryUUID: UUID): Category?
    @Insert(entity = Category::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(category: Category)
    @Update(entity = Category::class)
    suspend fun updateCategory(category: Category)
    @Delete(entity = Category::class)
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<Category>>
    @Query("SELECT * FROM category WHERE movement_type_id=(:transactionTypeId)")
    fun getCategoryListByTransactionType(transactionTypeId: Int): Flow<List<Category>>

    // Tag section
    @Query("SELECT * FROM tag WHERE id=(:id)")
    fun getTag(id: UUID): Tag?
    @Insert(entity = Tag::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTag(tag: Tag)
    @Update(entity = Tag::class)
    suspend fun updateTag(tag: Tag)
    @Delete(entity = Tag::class)
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getTagList(): Flow<List<Tag>>

    // Transaction section
    @Query("SELECT * FROM movement WHERE id=(:id)")
    suspend fun getTransaction(id: UUID): Transaction?
    @Insert(entity = Transaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTransaction(transaction: Transaction)
    @Update(entity = Transaction::class)
    suspend fun updateTransaction(transaction: Transaction)
    @Delete(entity = Transaction::class)
    suspend fun deleteTransaction(transaction: Transaction)
    @Query("SELECT * FROM movement WHERE (id_wallet IN (:idWalletList)) ORDER BY date DESC")
    fun getTransactionListInListOfWallet(idWalletList: List<UUID>): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE id_wallet=(:id_wallet) ORDER BY date DESC")
    fun getTransactionListInWallet(id_wallet: UUID): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE id_wallet=(:id_wallet) ORDER BY date DESC LIMIT :number_of_entries")
    fun getTransactionShortListInWallet(id_wallet: UUID,number_of_entries: Int): Flow<List<Transaction>>

    // Wallet section
    @Query("SELECT * FROM wallet WHERE id=(:id)")
    suspend fun getWallet(id: UUID): Wallet?
    @Insert(entity = Wallet::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addWallet(wallet: Wallet)
    @Update(entity = Wallet::class)
    suspend fun updateWallet(wallet: Wallet)
    @Delete(entity = Wallet::class)
    suspend fun deleteWallet(wallet: Wallet)

    @Query("SELECT * FROM wallet")
    fun getWalletList(): Flow<List<Wallet>>
    //@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    //@Query("SELECT *,MAX(last_access) FROM wallet")
    @Query("SELECT * FROM wallet ORDER BY last_access DESC LIMIT 1")
    suspend fun getWalletLastAccessed(): Wallet?

}