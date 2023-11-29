package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.math.min

/**
 * @param id ID of the tag, it coincides with TagTransaction.id
 * @param transactionUUID ID of the transaction
 * @param tagUUID ID of the TagEntry associated to the TagTransaction
 * @param name String coinciding with the name of the tag
 * @param count Times that the TagEntry is used in all transactions
 * @param enabled Flag that indicates whether the current tag is use by the transaction with id transactionUUID
 */
data class Tag (
    val id: UUID = UUID(0L,0L),
    val transactionUUID: UUID = UUID(0L, 0L),
    val tagUUID: UUID = UUID(0L, 0L),
    val name: String = "",
    val count: Int = 0,
    val enabled: Boolean = false,
) {

    companion object {

        /**
         *
         * Given a TagTransaction and a TagEntry, merge them into a Tag.
         *
         */
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

        /**
         * Given an existing tag entry, create a new Tag with TagEntry.name as Tag.name
         * @param transactionUUID ID of the transaction that will be associated to the TagEntry
         * @param tagEntry TagEntry to associate
         * @return Tag
         */
        fun newFromTagEntry(
            transactionUUID: UUID,
            tagEntry: TagEntry
        ): Tag {

            val tagTransaction = TagTransaction(transactionUUID = transactionUUID)
            return merge(tagTransaction,tagEntry)!!
        }

    }

    /**
     * @return
     *
     * True if the Tag has been newly created
     *
     * False if the Tag has been fetched from the data source
     */
    fun isNewTag(): Boolean = id == UUID(0L, 0L)

    /**
     * Separate the current tag into the single parts TagTransaction and TagEntry
     */
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

/**
 *
 * The data class traces all the tags linked to one single transaction.
 * There could be more transactions with the same tag, hence this data structure can avoid repetition of the same tag on multiple transactions.
 *
 * @param id ID of the tag linked to the specific transaction.
 * @param transactionUUID ID of the transaction that has the current tag.
 * @param tagUUID ID of the TagEntry linked to the current TagTransaction.
 */
@Entity(tableName = "tag_transaction")
data class TagTransaction (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_movement")
    val transactionUUID: UUID = UUID(0L, 0L),
    @ColumnInfo(name = "id_tag")
    val tagUUID: UUID = UUID(0L, 0L),
)

/**
 *
 *
 * @param id ID of the TagEntry.
 * @param name String containing the name of the tag.
 * @param count times that the tag is used in all the transactions registered in the application.
 *
 */
@Entity(tableName = "tag_entry")
data class TagEntry(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val count: Int = 0,
) {
    companion object {

        /**
         *
         * Given a string and a list of TagEntry, filter the list based on the string
         *
         * @param text Takes the string that has to match the initials of the tag name
         * @param tagList list of TagEntry that has to be filtered based on text
         *
         */
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