package com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.AddNewWallet
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.DeleteWallet
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetLastAccessedWallet
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetListOfNamesOfWallets
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetUsedCurrencies
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetWallet
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetWalletList
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.GetWalletListByCurrency
import com.zhengzhou.cashflow.database.api.use_case.implementations.walletUseCases.features.UpdateWallet
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.WalletUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.AddNewWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.DeleteWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetLastAccessedWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetListOfNamesOfWalletsInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetUsedCurrenciesInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListByCurrencyInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.GetWalletListInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.walletUseCases.features.UpdateWalletInterface

class WalletUseCases(
    private val repository: RepositoryInterface,
): WalletUseCasesInterface,
        AddNewWalletInterface by AddNewWallet(repository),
        DeleteWalletInterface by DeleteWallet(repository),
        GetLastAccessedWalletInterface by GetLastAccessedWallet(repository),
        GetListOfNamesOfWalletsInterface by GetListOfNamesOfWallets(repository),
        GetUsedCurrenciesInterface by GetUsedCurrencies(repository),
        GetWalletInterface by GetWallet(repository),
        GetWalletListInterface by GetWalletList(repository),
        GetWalletListByCurrencyInterface by GetWalletListByCurrency(repository),
        UpdateWalletInterface by UpdateWallet(repository)