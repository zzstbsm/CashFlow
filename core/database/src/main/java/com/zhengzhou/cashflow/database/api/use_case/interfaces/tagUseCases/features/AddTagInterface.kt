package com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features

import com.zhengzhou.cashflow.data.Tag

interface AddTagInterface {

    /**
     * addTag allows the user to add a tag in the database
     *
     * @param tag is the tag to insert in the database
     */
    suspend fun addTag(tag: Tag): Tag
}