package com.zhengzhou.cashflow.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey val id: UUID = UUID(0L,0L),
    val name: String = "",
    @ColumnInfo(name = "start_amount")
    val startAmount: Float = 0f,
    @ColumnInfo(name = "icon_id")
    val iconName: IconsMappedForDB = IconsMappedForDB.HOME,
    val currency: Currency = Currency.EUR,
    @ColumnInfo(name = "creation_date")
    val creationDate: Date = Date(),
    @ColumnInfo(name = "last_access")
    val lastAccess: Date = Date(),
    @ColumnInfo(name = "budget_enabled")
    val budgetEnabled: Boolean = false,
) : Parcelable {

    companion object {
        fun emptyWallet() : Wallet {
            return Wallet().copy(
                id = UUID(0L,0L),
                // TODO: next line to remove after the implementation of EditWalletScreen
                startAmount = 0f,
                iconName = IconsMappedForDB.WALLET,
            )
        }

        fun loadingWallet() : Wallet {
            return Wallet().copy(
                name = "Loading Wallet"
            )
        }
    }
}

data class WalletSelection(
    val wallet: Wallet = Wallet(),
    var toShow: Boolean = false
) {
    fun invertToShow() {
        toShow = !toShow
    }
}

enum class WalletIcon(
    @StringRes val iconName: Int,
    @DrawableRes val iconImage: Int
) {
    Wallet(
        iconName = R.string.wallet,
        iconImage = R.drawable.ic_wallet
    )
}