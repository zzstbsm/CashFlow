package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey
    @Contextual
    val id: UUID,
    val name: String = "",
    @ColumnInfo(name = "start_amount")
    val startAmount: Float,
    @ColumnInfo(name = "icon_id")
    val iconName: IconsMappedForDB,
    val currency: Currency,
    @ColumnInfo(name = "creation_date")
    @Contextual
    val creationDate: Date,
    @ColumnInfo(name = "last_access")
    @Contextual
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
                iconName = IconsMappedForDB.WALLET,
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