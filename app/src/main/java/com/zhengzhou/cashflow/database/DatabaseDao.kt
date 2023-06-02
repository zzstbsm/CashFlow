package com.zhengzhou.cashflow.database

import androidx.room.*
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DatabaseDao {

    // Category section
    @Query("SELECT * FROM category WHERE id=(:categoryUUID)")
    suspend fun getCategory(categoryUUID: UUID): Category
    @Insert(entity = Category::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(category: Category)
    @Update(entity = Category::class)
    suspend fun updateCategory(category: Category)
    @Delete(entity = Category::class)
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<Category>>

    // Tag section
    @Query("SELECT * FROM tag WHERE id=(:id)")
    fun getTag(id: UUID): Tag
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
    suspend fun getTransaction(id: UUID): Transaction
    @Insert(entity = Transaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTransaction(transaction: Transaction)
    @Update(entity = Transaction::class)
    suspend fun updateTransaction(transaction: Transaction)
    @Delete(entity = Transaction::class)
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM movement WHERE id_wallet=(:id_wallet) ORDER BY date DESC")
    fun getTransactionListInWallet(id_wallet: UUID): Flow<List<Transaction>>

    // Wallet section
    @Query("SELECT * FROM wallet WHERE id=(:id)")
    suspend fun getWallet(id: UUID): Wallet
    @Insert(entity = Wallet::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addWallet(wallet: Wallet)
    @Update(entity = Wallet::class)
    suspend fun updateWallet(wallet: Wallet)
    @Delete(entity = Wallet::class)
    suspend fun deleteWallet(wallet: Wallet)

    @Query("SELECT * FROM wallet")
    fun getWalletList(): Flow<List<Wallet>>
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT *,MAX(last_access) FROM wallet")
    fun getWalletLastAccessed(): Wallet

}