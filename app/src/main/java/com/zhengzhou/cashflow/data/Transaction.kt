package com.zhengzhou.cashflow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.R
import java.util.*

@Entity(tableName = "movement")
data class Transaction (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_wallet")
    val idWallet: UUID = UUID(0L,0L),
    val amount: Float = 0f,
    val date: Date = Date(),
    @ColumnInfo(name = "id_category")
    val idCategory: UUID = UUID(0L,0L),
    val description: String = "",
    @ColumnInfo(name = "id_location")
    val idLocation: UUID = UUID(0L,0L),
    @ColumnInfo(name = "movement_type")
    val movementType: Int = TransactionType.Loading.id
)

enum class TransactionType (
    val id: Int,
    @StringRes val text: Int,
    @StringRes val new_text: Int,
    @DrawableRes val iconId: Int,
) {
    Loading(
        id = 0,
        text = R.string.loading,
        new_text = R.string.loading,
        iconId = R.drawable.ic_error,
    ),
    Move(
        id = 1,
        text = R.string.move,
        new_text = R.string.new_move,
        iconId = R.drawable.ic_transfer
    ),
    Deposit(
        id = 2,
        text = R.string.deposit,
        new_text = R.string.new_deposit,
        iconId = R.drawable.ic_add
    ),
    Expense(
        id = 3,
        text = R.string.expense,
        new_text = R.string.new_expense,
        iconId = R.drawable.ic_remove
    )
}

fun setTransaction(
    id: Int
) : TransactionType? {

    // Return the type of transaction
    TransactionType.values().forEach { transactionType: TransactionType ->
        if (transactionType.id == id) {
            return transactionType
        }
    }

    // The id is not valid
    return null

}