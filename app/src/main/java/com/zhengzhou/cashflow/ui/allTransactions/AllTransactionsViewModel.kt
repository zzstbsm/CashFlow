package com.zhengzhou.cashflow.ui.allTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.dataForUi.TransactionAndCategory
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.TimeTools
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.UUID

data class AllTransactionsUiState(
    val isLoading: Boolean = true,

    val transactionListToShow: List<TransactionAndCategory> = listOf(),
    val dateTabWithIndexList: List<DateTabWithIndex> = listOf(),
    val shownTab: Int = -1
)

class AllTransactionsViewModel(
    walletUUID: UUID,
    categoryUUID: UUID,
    currency: Currency,
): ViewModel() {

    private var _uiState = MutableStateFlow(AllTransactionsUiState())
    val uiState: StateFlow<AllTransactionsUiState> = _uiState.asStateFlow()

    private var writingOnUiState: Boolean = false
    private val currencyFormatter: NumberFormat

    val repository = DatabaseRepository.get()

    init {

        currencyFormatter = Currency.setCurrencyFormatter(currencyName = currency.name)

        viewModelScope.launch {
            repository.getWalletListByCurrency(currency).collect { walletList ->
                repository.getTransactionListInListOfWallet(walletList).collect { transactionList ->
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

        transactionListToShow: List<TransactionAndCategory>? = null,
        dateTabWithIndexList: List<DateTabWithIndex>? = null,
        shownTab: Int? = null,
    ) {
        viewModelScope.launch {

            while (writingOnUiState) delay(5)

            writingOnUiState = true
            _uiState.value = uiState.value.copy(
                isLoading = isLoading ?: uiState.value.isLoading,

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
                category = repository.getCategory(it.categoryUUID) ?: Category.newEmpty()
            )
        }
    }

    fun getCurrencyFormatter() = currencyFormatter

    fun updateShownTab(tabIndex: Int) {
        setUiState(
            shownTab = tabIndex
        )
    }

}