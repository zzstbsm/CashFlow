package com.zhengzhou.cashflow.database

import androidx.room.*
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Location
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.TagTransaction
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DatabaseDao {

    // Category section
    @Query("SELECT * FROM category WHERE id=(:categoryUUID)")
    suspend fun getCategory(categoryUUID: UUID): Category?
    @Insert(entity = Category::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(category: Category)
    @Update(entity = Category::class)
    suspend fun updateCategory(category: Category)
    @Delete(entity = Category::class)
    suspend fun deleteCategory(category: Category)
    @Query("SELECT count(*) FROM movement WHERE id_category=(:categoryUUID)")
    suspend fun getCategoryOccurrences(categoryUUID: UUID): Int

    // Location section
    @Query("SELECT * FROM location WHERE id=(:locationUUID)")
    suspend fun getLocation(locationUUID: UUID): Location?
    @Insert(entity = Location::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addLocation(location: Location)
    @Update(entity = Location::class)
    suspend fun updateLocation(location: Location)
    @Delete(entity = Location::class)
    suspend fun deleteLocation(location: Location)

    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<Category>>
    @Query("SELECT * FROM category WHERE movement_type_id=(:transactionTypeId)")
    fun getCategoryListByTransactionType(transactionTypeId: Int): Flow<List<Category>>

    // TagEntry section
    @Query("SELECT * FROM tag_entry WHERE id=(:tagUUID)")
    suspend fun getTagEntry(tagUUID: UUID): TagEntry?
    @Insert(entity = TagEntry::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTagEntry(tag: TagEntry)
    @Update(entity = TagEntry::class)
    suspend fun updateTagEntry(tag: TagEntry)
    @Query("UPDATE tag_entry SET count = count + 1 WHERE id=(:tagUUID)")
    suspend fun updateTagEntryIncreaseCounter(tagUUID: UUID)
    @Query("UPDATE tag_entry SET count = count - 1 WHERE id=(:tagUUID)")
    suspend fun updateTagEntryDecreaseCounter(tagUUID: UUID)
    @Delete(entity = TagEntry::class)
    suspend fun deleteTagEntry(tag: TagEntry)

    @Query("SELECT * FROM tag_entry")
    fun getTagEntryList(): Flow<List<TagEntry>>

    // TagTransaction section
    @Query("SELECT * FROM tag_transaction WHERE id=(:tagTransactionUUID)")
    suspend fun getTagTransaction(tagTransactionUUID: UUID): TagTransaction?
    @Insert(entity = TagTransaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTagTransaction(tagTransaction: TagTransaction)
    @Update(entity = TagTransaction::class)
    suspend fun updateTagTransaction(tagTransaction: TagTransaction)
    @Delete(entity = TagTransaction::class)
    suspend fun deleteTagTransaction(tagTransaction: TagTransaction)
    @Query("SELECT * FROM tag_transaction WHERE id_movement=(:transactionUUID)")
    fun getTagTransactionFromTransaction(transactionUUID: UUID): Flow<List<TagTransaction>>

    // Transaction section
    @Query("SELECT * FROM movement WHERE id=(:transactionUUID)")
    suspend fun getTransaction(transactionUUID: UUID): Transaction?
    @Insert(entity = Transaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTransaction(transaction: Transaction)
    @Update(entity = Transaction::class)
    suspend fun updateTransaction(transaction: Transaction)
    @Delete(entity = Transaction::class)
    suspend fun deleteTransaction(transaction: Transaction)
    @Query("SELECT * FROM movement WHERE (id_wallet IN (:idWalletList) AND is_blueprint=0) ORDER BY date DESC")
    fun getTransactionListInListOfWallet(idWalletList: List<UUID>): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE (id_wallet=(:idWallet) AND is_blueprint=0) ORDER BY date DESC")
    fun getTransactionListInWallet(idWallet: UUID): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE (id_wallet=(:idWallet) AND is_blueprint=0) ORDER BY date DESC LIMIT :numberOfEntries")
    fun getTransactionShortListInWallet(idWallet: UUID,numberOfEntries: Int): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE is_blueprint != 0")
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>

    // Wallet section
    @Query("SELECT * FROM wallet WHERE id=(:walletUUID)")
    suspend fun getWallet(walletUUID: UUID): Wallet?
    @Insert(entity = Wallet::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addWallet(wallet: Wallet)
    @Update(entity = Wallet::class)
    suspend fun updateWallet(wallet: Wallet)
    @Delete(entity = Wallet::class)
    suspend fun deleteWallet(wallet: Wallet)

    @Query("SELECT DISTINCT currency FROM wallet")
    fun getWalletCurrencyList(): Flow<List<Currency>>
    @Query("SELECT * FROM wallet")
    fun getWalletList(): Flow<List<Wallet>>
    @Query("SELECT * FROM wallet WHERE currency=(:currency)")
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>>
    @Query("SELECT name FROM wallet")
    fun getWalletListOfNames(): Flow<List<String>>
    @Query("SELECT * FROM wallet ORDER BY last_access DESC LIMIT 1")
    suspend fun getWalletLastAccessed(): Wallet?

}