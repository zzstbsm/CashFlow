package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces

import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.AddWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.DeleteWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.GetWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.UpdateWalletInterface

interface WalletUseCasesInterface: AddWalletInterface,
        DeleteWalletInterface,
        GetWalletInterface,
        UpdateWalletInterface