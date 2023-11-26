package com.zhengzhou.cashflow.database.databaseDaoComponents

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface TransactionDao {

    // Default implementations
    @Query("SELECT * FROM movement WHERE id=(:transactionUUID)")
    suspend fun getTransaction(transactionUUID: UUID): Transaction?
    @Insert(entity = Transaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTransaction(transaction: Transaction)
    @Update(entity = Transaction::class)
    suspend fun updateTransaction(transaction: Transaction)
    @Delete(entity = Transaction::class)
    suspend fun deleteTransaction(transaction: Transaction)

    // Custom query based on the application
    @Query("SELECT * FROM movement " +
                   "WHERE (id_wallet IN (:idWalletList) AND is_blueprint=0 AND id_secondary_wallet=(:secondaryWalletId)) " +
                   "ORDER BY date DESC")
    fun getTransactionListInListOfWallet(idWalletList: List<UUID>, secondaryWalletId: UUID = UUID(0L, 0L)): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE (id_wallet=(:walletUUID) OR id_secondary_wallet=(:walletUUID) AND is_blueprint=0) ORDER BY date DESC")
    fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>>
    @Query("SELECT * FROM movement WHERE is_blueprint != 0")
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>
}