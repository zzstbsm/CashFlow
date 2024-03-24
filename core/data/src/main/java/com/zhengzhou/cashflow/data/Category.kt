package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import java.util.UUID

@Entity(tableName = "category")
data class Category (
    @PrimaryKey val id: UUID,
    var name: String,
    @ColumnInfo(name = "id_icon")
    val iconName: IconsMappedForDB,
    @ColumnInfo(name = "movement_type")
    val transactionType: TransactionType,
) {
    companion object {
        fun newEmpty(): Category {
            return Category(
                id = UUID(0L,0L),
                name = "",
                iconName = IconsMappedForDB.LOADING,
                transactionType = TransactionType.Loading,
            )
        }

        fun newTransfer(categoryUUID: UUID): Category {
            return newEmpty().copy(
                id = categoryUUID,
                iconName = IconsMappedForDB.TRANSFER,
                transactionType = TransactionType.Move
            )
        }
    }
}