package com.zhengzhou.cashflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag")
data class Tag (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val count: Int = 0,
) {
    companion object {

        fun tagListFiltered(text: String, tagList: List<Tag>): List<Tag> {

            val tempList = mutableListOf<Tag>()

            if (text == "") {
                return tagList
            }

            tagList.forEach {tag: Tag ->
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
            return tempList.toList()
        }
    }
}