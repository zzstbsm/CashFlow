package com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features.AddWallet
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features.DeleteWallet
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features.GetWallet
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.features.UpdateWallet
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.WalletUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.AddWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.DeleteWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.GetWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.interfaces.features.UpdateWalletInterface

class WalletUseCases(
    private val repository: RepositoryInterface,
): WalletUseCasesInterface,
        AddWalletInterface by AddWallet(repository),
        DeleteWalletInterface by DeleteWallet(repository),
        GetWalletInterface by GetWallet(repository),
        UpdateWalletInterface by UpdateWallet(repository)