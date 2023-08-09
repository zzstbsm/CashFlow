package com.zhengzhou.cashflow.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey val id: UUID,
    val name: String = "",
    @ColumnInfo(name = "start_amount")
    val startAmount: Float,
    @ColumnInfo(name = "icon_id")
    val iconName: IconsMappedForDB,
    val currency: Currency,
    @ColumnInfo(name = "creation_date")
    val creationDate: Date,
    @ColumnInfo(name = "last_access")
    val lastAccess: Date,
    @ColumnInfo(name = "budget_enabled")
    val budgetEnabled: Boolean,
) : Parcelable {

    companion object {
        fun newEmpty() : Wallet {
            return Wallet(
                id = UUID(0L,0L),
                name = "",
                startAmount = 0f,
                iconName = IconsMappedForDB.HOME,
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
    }
}