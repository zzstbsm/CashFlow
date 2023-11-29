package com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases

import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.AddNewWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.DeleteWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetLastAccessedWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetListOfNamesOfWalletsInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetUsedCurrenciesInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListByCurrencyInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.UpdateWalletInterface

interface WalletUseCasesInterface: AddNewWalletInterface,
        DeleteWalletInterface,
        GetLastAccessedWalletInterface,
        GetListOfNamesOfWalletsInterface,
        GetUsedCurrenciesInterface,
        GetWalletInterface,
        GetWalletListByCurrencyInterface,
        GetWalletListInterface,
        UpdateWalletInterface