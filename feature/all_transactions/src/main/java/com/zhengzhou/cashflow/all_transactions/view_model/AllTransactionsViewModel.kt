package com.zhengzhou.cashflow.all_transactions.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.all_transactions.DateTabWithIndex
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.complex_data.TransactionAndCategory
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.TransactionUseCases
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.tools.TimeTools
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class AllTransactionsViewModel(
    repository: RepositoryInterface,
    walletUUID: UUID,
    categoryUUID: UUID,
    currency: Currency,
): ViewModel() {

    private var _uiState = MutableStateFlow(AllTransactionsUiState())
    val uiState: StateFlow<AllTransactionsUiState> = _uiState.asStateFlow()

    private var writingOnUiState: Boolean = false

    private val categoryUseCases = CategoryUseCases(repository)
    private val transactionUseCases = TransactionUseCases(repository)
    private val walletUseCases = WalletUseCases(repository)

    init {

        viewModelScope.launch {
            walletUseCases.getWalletListByCurrency(currency).collect { walletList ->
                transactionUseCases.getTransactionListInListOfWallet(walletList).collect { transactionList ->
                    val transactionListToShow = processTransactionListToShow(
                        transactionList = transactionList,
                        walletUUID = walletUUID,
                        categoryUUID = categoryUUID,
                    )

                    val dateTabWithIndexList = processDateTabWithIndex(
                        sortedDescendingDateTransactionListToShow = transactionListToShow
                    )

                    setUiState(
                        isLoading = false,
                        currency = currency,
                        transactionListToShow = transactionListToShow,
                        dateTabWithIndexList = dateTabWithIndexList,
                        shownTab = dateTabWithIndexList.size-1,
                    )
                }
            }

        }
    }

    private fun setUiState(
        isLoading: Boolean? = null,

        currency: Currency? = null,
        transactionListToShow: List<TransactionAndCategory>? = null,
        dateTabWithIndexList: List<DateTabWithIndex>? = null,
        shownTab: Int? = null,
    ) {
        viewModelScope.launch {

            while (writingOnUiState) delay(5)

            writingOnUiState = true
            _uiState.value = uiState.value.copy(
                isLoading = isLoading ?: uiState.value.isLoading,

                currency = currency ?: uiState.value.currency,
                transactionListToShow = transactionListToShow ?: uiState.value.transactionListToShow,
                dateTabWithIndexList = dateTabWithIndexList ?: uiState.value.dateTabWithIndexList,
                shownTab = shownTab ?: uiState.value.shownTab,
            )
            writingOnUiState = false
        }
    }

    private fun processDateTabWithIndex(sortedDescendingDateTransactionListToShow: List<TransactionAndCategory>): List<DateTabWithIndex> {

        val dateTabWithIndexList: MutableList<DateTabWithIndex> = mutableListOf()

        val calendarLastDayOfMonth = Calendar.getInstance()
        calendarLastDayOfMonth.time = TimeTools.getNextMonth(
            TimeTools.getFirstDayOfCurrentMonth()
        )

        sortedDescendingDateTransactionListToShow.forEachIndexed { index, transactionAndCategory ->
            // Process only if the current considered transaction.date is before the considered month
            if (calendarLastDayOfMonth.time > transactionAndCategory.transaction.date) {
                // Go to previous month
                calendarLastDayOfMonth.add(Calendar.MONTH, -1)

                dateTabWithIndexList.add(
                    DateTabWithIndex(
                        date = calendarLastDayOfMonth.time,
                        indexOfTransaction = index,
                        indexOfTab = 0 // Fixed in the following for loop,
                    )
                )
            }
        }

        // Fix indexOfTab
        for (index in 0 until dateTabWithIndexList.size) {
            dateTabWithIndexList[index] = dateTabWithIndexList[index].copy(
                indexOfTab = dateTabWithIndexList.size - index - 1
            )
        }

        return dateTabWithIndexList.sortedBy { it.date }
    }

    private suspend fun processTransactionListToShow(
        transactionList: List<Transaction>,
        walletUUID: UUID,
        categoryUUID: UUID,
    ): List<TransactionAndCategory> {
        return transactionList.filter {
            if (walletUUID != UUID(0L,0L)) {
                it.walletUUID == walletUUID || it.secondaryWalletUUID == walletUUID
            } else true
        }.filter {
            if (categoryUUID != UUID(0L,0L)) {
                it.categoryUUID == categoryUUID
            } else true
        }.sortedByDescending { it.date }.map {
            TransactionAndCategory(
                transaction = it,
                category = categoryUseCases.getCategory(it.categoryUUID) ?: Category.newEmpty()
            )
        }
    }

    fun updateShownTab(tabIndex: Int) {
        setUiState(
            shownTab = tabIndex
        )
    }

}