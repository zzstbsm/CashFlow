package com.zhengzhou.cashflow.database.data.data_source.dao.class_dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface WalletDao {

    // Default implementations
    @Query("SELECT * FROM wallet WHERE id=(:walletUUID)")
    suspend fun getWallet(walletUUID: UUID): Wallet?
    @Insert(entity = Wallet::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addWallet(wallet: Wallet)
    @Update(entity = Wallet::class)
    suspend fun updateWallet(wallet: Wallet)
    @Delete(entity = Wallet::class)
    suspend fun deleteWallet(wallet: Wallet)

    // Custom query based on the application
    @Query("SELECT DISTINCT currency FROM wallet")
    fun getWalletCurrencyList(): Flow<List<Currency>>
    @Query("SELECT * FROM wallet ORDER BY last_access DESC LIMIT 1")
    suspend fun getWalletLastAccessed(): Wallet?
    @Query("SELECT * FROM wallet")
    fun getWalletList(): Flow<List<Wallet>>
    @Query("SELECT * FROM wallet WHERE currency=(:currency)")
    fun getWalletListByCurrency(currency: Currency): Flow<List<Wallet>>
    @Query("SELECT name FROM wallet")
    fun getWalletListOfNames(): Flow<List<String>>
}