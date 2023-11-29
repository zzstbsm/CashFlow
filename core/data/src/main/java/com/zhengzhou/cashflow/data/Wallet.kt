package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import java.util.Date
import java.util.UUID

@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey val id: UUID,
    val name: String = "",
    @ColumnInfo(name = "start_amount")
    val startAmount: Float,
    @ColumnInfo(name = "icon_id")
    val iconName: com.zhengzhou.cashflow.themes.IconsMappedForDB,
    val currency: Currency,
    @ColumnInfo(name = "creation_date")
    val creationDate: Date,
    @ColumnInfo(name = "last_access")
    val lastAccess: Date,
    @ColumnInfo(name = "budget_enabled")
    val budgetEnabled: Boolean,
) {

    companion object {
        fun newEmpty() : Wallet {
            return Wallet(
                id = UUID(0L,0L),
                name = "",
                startAmount = 0f,
                iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.WALLET,
                currency = Currency.EUR,
                creationDate = Date(),
                lastAccess = Date(),
                budgetEnabled = false,
            )
        }

        fun loadingWallet() : Wallet {
            return newEmpty().copy(
                name = "Loading Wallet"
            )
        }

        fun newWalletId() = UUID(0L,0L)
    }
}