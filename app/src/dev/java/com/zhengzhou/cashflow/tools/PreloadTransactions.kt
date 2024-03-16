package com.zhengzhou.cashflow.core.tools

import com.zhengzhou.cashflow.database.PrepopulateDatabase
import com.zhengzhou.cashflow.database.api.DatabaseInstance
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreloadTransactions {

    companion object {
        fun load() {

            val repository = DatabaseInstance.getRepository()
            val walletUseCases = WalletUseCases(repository = repository)

            val coroutinePreloadData = CoroutineScope(Dispatchers.Default)
            coroutinePreloadData.launch {
                if (walletUseCases.getLastAccessedWallet() == null) PrepopulateDatabase()
            }
        }
    }
}