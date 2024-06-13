package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
@Entity(tableName = "movement")
data class Transaction (
    @PrimaryKey
    @Contextual
    val id: UUID,
    @ColumnInfo(name = "id_wallet")
    @Contextual
    val walletUUID: UUID,
    @ColumnInfo(name = "id_secondary_wallet")
    @Contextual
    val secondaryWalletUUID: UUID,
    val amount: Float,
    @Contextual
    val date: Date,
    @ColumnInfo(name = "id_category")
    @Contextual
    val categoryUUID: UUID,
    val description: String,
    @ColumnInfo(name = "id_location")
    @Contextual
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

        fun newTransactionUUID(): UUID {
            return UUID(0L, 0L)
        }
    }
}