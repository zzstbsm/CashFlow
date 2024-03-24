package com.zhengzhou.cashflow.total_balance.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.complex_data.TransactionAndCategory
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.TransactionUseCases
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.tools.TimeTools
import com.zhengzhou.cashflow.total_balance.BalanceTabOptions
import com.zhengzhou.cashflow.total_balance.TimeFilterForSegmentedButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

internal class BalanceViewModel(
    repository: RepositoryInterface
) : ViewModel() {

    private val categoryUseCases = CategoryUseCases(repository)
    private val transactionUseCases = TransactionUseCases(repository)
    private val walletUseCases = WalletUseCases(repository)

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private var writingOnUiState: Boolean = false

    private var jobGetCategoryList: Job
    private var jobGetCurrency: Job
    private var jobGetTransactionList: Job
    private var jobGetWalletList: Job

    init {

        jobGetCategoryList = getCategories()
        jobGetCurrency = getCurrencyList()
        jobGetWalletList = getWalletList(uiState.value.equivalentWallet.currency)
        jobGetTransactionList = getTransactionList()

    }

    private fun setUiState(
        isLoading: Boolean? = null,

        equivalentWallet: Wallet? = null,

        walletList: List<Wallet>? = null,
        currencyList: List<Currency>? = null,

        categoryList: List<Category>? = null,

        transactionList: List<TransactionAndCategory>? = null,
        transactionListToShow: List<TransactionAndCategory>? = null,

        filterStartDate: Date? = null,
        filterEndDate: Date? = null,
        timeFilter: TimeFilterForSegmentedButton? = null,
        setTimeFilter: Boolean = false,

        shownTab: BalanceTabOptions? = null,
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)

            writingOnUiState = true

            val timeFilterValue = if (setTimeFilter && timeFilter == null) null else timeFilter ?: uiState.value.timeFilter

            _uiState.value = BalanceUiState(
                isLoading= isLoading ?: uiState.value.isLoading,

                equivalentWallet = equivalentWallet ?: uiState.value.equivalentWallet,

                walletList = walletList ?: uiState.value.walletList,
                currencyList = currencyList ?: uiState.value.currencyList,

                categoryList = categoryList ?: uiState.value.categoryList,

                transactionList = transactionList ?: uiState.value.transactionList,
                transactionListToShow = transactionListToShow ?: uiState.value.transactionListToShow,

                filterStartDate = filterStartDate ?: uiState.value.filterStartDate,
                filterEndDate = filterEndDate ?: uiState.value.filterEndDate,
                timeFilter = timeFilterValue,

                shownTab = shownTab ?: uiState.value.shownTab,
            )

            writingOnUiState = false
        }
    }

    fun onEvent(event: BalanceEvent) {
        when (event) {
            is BalanceEvent.SelectTabToShow -> {
                setUiState(
                    shownTab = event.selectedTab,
                )
            }
            is BalanceEvent.SetTimeFilter -> {

                val toSaveStartDate: Date
                val toSaveEndDate: Date

                if (event.timeFilter == null || event.navigation) {
                    toSaveStartDate = TimeTools.timeSetBeginningOfDay(event.startDate)
                    toSaveEndDate = TimeTools.timeSetEndOfDay(event.endDate)
                } else {
                    toSaveStartDate = event.timeFilter.getStartDate()
                    toSaveEndDate = event.timeFilter.getEndDate()
                }
                setUiState(
                    transactionListToShow = getFilteredTransactionList(
                        toSaveStartDate,toSaveEndDate
                    ),
                    filterStartDate = toSaveStartDate,
                    filterEndDate = toSaveEndDate,
                    timeFilter = event.timeFilter,
                    setTimeFilter = true,
                )
            }
            is BalanceEvent.SetWalletListByCurrency -> {
                setUiState(
                    isLoading = true
                )
                jobGetWalletList.cancel()
                jobGetWalletList = getWalletList(currency = event.currency)

                viewModelScope.launch {
                    jobGetTransactionList = getTransactionList()
                }
            }
        }
    }

    private fun getCategories(): Job {
        return viewModelScope.launch {
            categoryUseCases.getCategoryList().collect { categoryList ->
                setUiState(
                    categoryList = categoryList.sortedBy { category: Category -> category.name },
                )
            }
        }
    }

    private fun getCurrencyList(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            // Collect all wallets
            walletUseCases.getUsedCurrenciesInAllWallets().collect { currencyList ->
                setUiState(
                    currencyList = currencyList,
                    isLoading = false
                )
            }
        }
    }
    private fun getFilteredTransactionList(
        startDate: Date = uiState.value.filterStartDate,
        endDate: Date = uiState.value.filterEndDate,
    ): List<TransactionAndCategory> {
        return uiState.value.transactionList.filter { transactionAndCategory ->
            transactionAndCategory.transaction.date in startDate..endDate
        }
    }
    private fun getTransactionList(): Job {
        return viewModelScope.launch {
            while (uiState.value.isLoading) delay(5)

            setUiState(
                transactionList = listOf()
            )
            transactionUseCases.getTransactionListInListOfWallet(uiState.value.walletList).collect { transactionList ->

                val transactionCategoryGroup: MutableList<TransactionAndCategory> = mutableListOf()

                transactionList.forEach { transaction ->
                    val category = categoryUseCases.getCategory(transaction.categoryUUID) ?: Category.newEmpty()
                    transactionCategoryGroup.add(
                        TransactionAndCategory(
                            transaction = transaction,
                            category = category,
                        )
                    )
                }

                setUiState(
                    transactionList = transactionCategoryGroup,
                )
                onEvent(
                    BalanceEvent.SetTimeFilter(
                        timeFilter = uiState.value.timeFilter,
                        startDate = uiState.value.filterStartDate,
                        endDate = uiState.value.filterEndDate,
                        navigation = false,
                    )
                )
            }

        }
    }
    private fun getWalletList(currency: Currency): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            setUiState(
                isLoading = true
            )
            // Collect all wallets
            walletUseCases.getWalletListByCurrency(currency).collect { collectedWalletList ->
                var amount = 0f
                collectedWalletList.forEach { wallet: Wallet ->
                    amount += wallet.startAmount
                }
                setUiState(
                    equivalentWallet = uiState.value.equivalentWallet.copy(
                        currency = currency,
                        startAmount = amount,
                    ),
                    walletList = collectedWalletList,
                    isLoading = false
                )
            }
        }
    }

}
