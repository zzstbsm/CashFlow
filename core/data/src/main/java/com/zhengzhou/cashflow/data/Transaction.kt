package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "movement")
data class Transaction (
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "id_wallet")
    val walletId: UUID,
    @ColumnInfo(name = "id_secondary_wallet")
    val secondaryWalletId: UUID,
    val amount: Float,
    val date: Date,
    @ColumnInfo(name = "id_category")
    val categoryId: UUID,
    val description: String,
    @ColumnInfo(name = "id_location")
    val locationId: UUID?,
    @ColumnInfo(name = "movement_type")
    val transactionType: TransactionType,
    @ColumnInfo(name = "is_blueprint")
    val isBlueprint: Boolean,
) {

    companion object {
        fun newEmpty(): Transaction {
            return Transaction(
                id = UUID(0L,0L),
                walletId = UUID(0L, 0L),
                secondaryWalletId = UUID(0L, 0L),
                amount = 0f,
                date = Date(),
                categoryId = UUID(0L,0L),
                description = "",
                locationId = null,
                transactionType = TransactionType.Loading,
                isBlueprint = false,
            )
        }
    }
}