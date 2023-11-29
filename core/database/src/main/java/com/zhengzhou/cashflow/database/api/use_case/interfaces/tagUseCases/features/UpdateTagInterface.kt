package com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features

import com.zhengzhou.cashflow.data.Tag

interface UpdateTagInterface {
    suspend fun updateTag(tag: Tag)
}