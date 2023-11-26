package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.math.min

data class Tag (
    val id: UUID = UUID(0L,0L),
    val transactionUUID: UUID = UUID(0L, 0L),
    val tagUUID: UUID = UUID(0L, 0L),
    val name: String = "",
    val count: Int = 0,
    val enabled: Boolean = false,
) {

    companion object {

        fun merge(
            tagTransaction: TagTransaction?,
            tagEntry: TagEntry?,
        ): Tag? {

            if (tagTransaction == null || tagEntry == null) return null

            return Tag(
                id = tagTransaction.id,
                transactionUUID = tagTransaction.transactionUUID,
                tagUUID = tagEntry.id,
                name = tagEntry.name,
                count = tagEntry.count,
                enabled = true,
            )
        }

        fun newFromTagEntry(
            transactionUUID: UUID,
            tagEntry: TagEntry
        ): Tag {

            val tagTransaction = TagTransaction(transactionUUID = transactionUUID)
            return merge(tagTransaction,tagEntry)!!
        }

    }

    fun isNewTag(): Boolean = id == UUID(0L, 0L)

    fun separate(): Pair<TagTransaction, TagEntry> {

        val tagTransaction = TagTransaction(
            id = id,
            transactionUUID = transactionUUID,
            tagUUID = tagUUID
        )
        val tagEntry = TagEntry(
            id = tagUUID,
            name = name,
            count = count,
        )

        return Pair(tagTransaction,tagEntry)

    }

    fun increaseCounter(): Tag {
        return this.copy(
            count = this.count + 1
        )
    }
    fun decreaseCounter(): Tag {
        return this.copy(
            count = this.count - 1
        )
    }
    fun enableTag(): Tag {
        return this.copy(
            enabled = true
        )
    }
    fun disableTag(): Tag {
        return this.copy(
            enabled = false
        )
    }
}

@Entity(tableName = "tag_transaction")
data class TagTransaction (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_movement")
    val transactionUUID: UUID = UUID(0L, 0L),
    @ColumnInfo(name = "id_tag")
    val tagUUID: UUID = UUID(0L, 0L),
)

@Entity(tableName = "tag_entry")
data class TagEntry(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val count: Int = 0,
) {
    companion object {

        fun tagListFiltered(text: String, tagList: List<TagEntry>): List<TagEntry> {

            val tempList = mutableListOf<TagEntry>()

            if (text == "") {
                return tagList
            }

            tagList.forEach {tag: TagEntry ->
                if (text.length > tag.name.length) return@forEach

                run breaking@ {
                    text.forEachIndexed { index, c ->
                        // Characters don't correspond
                        if (
                            tag.name[index] != c
                        ) return@breaking
                    }
                    // The text is in the first letters of the string in memory
                    tempList.add(tag)
                }

            }

            val toIndex = min(tempList.size,3)
            return if (tempList.isEmpty()) tempList.toList() else tempList.toList().sortedBy {
                it.name
            }.subList(fromIndex = 0, toIndex = toIndex)
        }
    }
}