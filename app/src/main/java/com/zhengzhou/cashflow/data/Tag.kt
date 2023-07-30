package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.math.min

data class Tag (
    val id: UUID = UUID(0L,0L),
    val idTransaction: UUID = UUID(0L,0L),
    val idTag: UUID = UUID(0L,0L),
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
                idTransaction = tagTransaction.idTransaction,
                idTag = tagEntry.id,
                name = tagEntry.name,
                count = tagEntry.count,
                enabled = true,
            )
        }

        fun newFromTagEntry(
            transactionUUID: UUID,
            tagEntry: TagEntry
        ): Tag {

            val tagTransaction = TagTransaction(idTransaction = transactionUUID)
            return this.merge(tagTransaction,tagEntry)!!
        }

    }

    fun isNewTag(): Boolean = id == UUID(0L, 0L)

    fun separate(): Pair<TagTransaction,TagEntry> {

        val tagTransaction = TagTransaction(
            id = id,
            idTransaction = idTransaction,
            idTag = idTag
        )
        val tagEntry = TagEntry(
            id = idTag,
            name = name,
            count = count,
        )

        return Pair(tagTransaction,tagEntry)

    }
}

@Entity(tableName = "tag_transaction")
data class TagTransaction (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_movement")
    val idTransaction: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_tag")
    val idTag: UUID = UUID(0L,0L),
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