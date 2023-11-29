package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features

import kotlinx.coroutines.flow.Flow

interface GetListOfNamesOfWalletsInterface{
    fun getListOfNames(): Flow<List<String>>
}