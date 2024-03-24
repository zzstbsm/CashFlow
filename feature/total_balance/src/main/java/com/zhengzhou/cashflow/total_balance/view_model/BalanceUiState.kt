package com.zhengzhou.cashflow.total_balance.view_model

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.complex_data.TransactionAndCategory
import com.zhengzhou.cashflow.total_balance.BalanceTabOptions
import com.zhengzhou.cashflow.total_balance.TimeFilterForSegmentedButton
import java.util.Date

internal data class BalanceUiState(
    val isLoading: Boolean = true,
    val equivalentWallet: Wallet = Wallet.newEmpty().copy(
        name = "All wallets",
        currency = Currency.EUR,
    ),
    val walletList: List<Wallet> = listOf(),
    val currencyList: List<Currency> = listOf(),
    val transactionList: List<TransactionAndCategory> = listOf(),
    val categoryList: List<Category> = listOf(),
    val transactionListToShow: List<TransactionAndCategory> = listOf(),

    val filterStartDate: Date = TimeFilterForSegmentedButton.Month.getStartDate(),
    val filterEndDate: Date = TimeFilterForSegmentedButton.Month.getEndDate(),
    val timeFilter: TimeFilterForSegmentedButton? = TimeFilterForSegmentedButton.Month,

    val shownTab: BalanceTabOptions = BalanceTabOptions.CATEGORIES,
) {

    fun getBalance(): Float {

        var amount: Float = this.updateEquivalentWallet().equivalentWallet.startAmount

        this.transactionList.map {
            it.transaction
        }.forEach { transaction ->
            amount += transaction.amount
        }
        return amount
    }

    fun getLastWallet(): Wallet {
        return this.walletList.maxByOrNull { wallet ->
            wallet.lastAccess
        } ?: Wallet.newEmpty()
    }

    private fun updateEquivalentWallet() : BalanceUiState {

        var startAmount = 0f

        this.walletList.forEach { wallet ->
            startAmount += wallet.startAmount
        }

        return this.copy(
            equivalentWallet = this.equivalentWallet.copy(
                startAmount = startAmount,
            )
        )

    }
}