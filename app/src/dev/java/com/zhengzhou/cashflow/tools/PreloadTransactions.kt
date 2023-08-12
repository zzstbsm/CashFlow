package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.database.PrepopulateDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreloadTransactions {

    companion object {
        fun load() {

            val repository = DatabaseRepository.get()
            val coroutinePreloadData = CoroutineScope(Dispatchers.Default)
            coroutinePreloadData.launch {
                if (repository.getWalletLastAccessed() == null) PrepopulateDatabase()
            }
        }
    }
}