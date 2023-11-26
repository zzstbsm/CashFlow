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
    val walletUUID: UUID,
    @ColumnInfo(name = "id_secondary_wallet")
    val secondaryWalletUUID: UUID,
    val amount: Float,
    val date: Date,
    @ColumnInfo(name = "id_category")
    val categoryUUID: UUID,
    val description: String,
    @ColumnInfo(name = "id_location")
    val locationUUID: UUID?,
    @ColumnInfo(name = "movement_type")
    val transactionType: TransactionType,
    @ColumnInfo(name = "is_blueprint")
    val isBlueprint: Boolean,
) {

    companion object {
        fun newEmpty(): Transaction {
            return Transaction(
                id = UUID(0L,0L),
                walletUUID = UUID(0L, 0L),
                secondaryWalletUUID = UUID(0L, 0L),
                amount = 0f,
                date = Date(),
                categoryUUID = UUID(0L, 0L),
                description = "",
                locationUUID = null,
                transactionType = TransactionType.Loading,
                isBlueprint = false,
            )
        }
    }
}