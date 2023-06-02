package com.zhengzhou.cashflow.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "Wallet",
    @ColumnInfo(name = "start_amount")
    val startAmount: Float = 0f,
    @ColumnInfo(name = "icon_id")
    @DrawableRes val iconId: Int = R.drawable.ic_wallet,
    val currency: String = "EUR",
    @ColumnInfo(name = "creation_date")
    val creationDate: Date = Date(),
    @ColumnInfo(name = "last_access")
    val lastAccess: Date = Date()
) : Parcelable {

    companion object {
        fun emptyWallet() : Wallet {
            return Wallet().copy(
                name = "New Wallet",
                // TODO: next line to remove after the implementation of EditWalletScreen
                startAmount = 100f,
                iconId = R.drawable.ic_wallet,
            )
        }

        fun loadingWallet() : Wallet {
            return Wallet().copy(
                name = "Loading Wallet"
            )
        }
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