package com.zhengzhou.cashflow.total_balance.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.TransactionUseCases
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.tools.TimeTools
import com.zhengzhou.cashflow.total_balance.BalanceTabOptions
import com.zhengzhou.cashflow.total_balance.R
import com.zhengzhou.cashflow.total_balance.data_structure.TransactionAndCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
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
                setTimeFilter(
                    timeFilter = uiState.value.timeFilter
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

    fun setTimeFilter(
        timeFilter: TimeFilterForSegmentedButton?,
        startDate: Date = uiState.value.filterStartDate,
        endDate: Date = uiState.value.filterEndDate,
        navigation: Boolean = false,
    ) {

        val toSaveStartDate: Date
        val toSaveEndDate: Date

        if (timeFilter == null || navigation) {
            toSaveStartDate = TimeTools.timeSetBeginningOfDay(startDate)
            toSaveEndDate = TimeTools.timeSetEndOfDay(endDate)
        } else {
            toSaveStartDate = timeFilter.getStartDate()
            toSaveEndDate = timeFilter.getEndDate()
        }
        setUiState(
            transactionListToShow = getFilteredTransactionList(
                toSaveStartDate,toSaveEndDate
            ),
            filterStartDate = toSaveStartDate,
            filterEndDate = toSaveEndDate,
            timeFilter = timeFilter,
            setTimeFilter = true,
        )
    }
    fun setWalletListByCurrency(currency: Currency) {
        setUiState(
            isLoading = true
        )
        jobGetWalletList.cancel()
        jobGetWalletList = getWalletList(currency = currency)

        viewModelScope.launch {
            jobGetTransactionList = getTransactionList()
        }
    }

    fun selectTabToShow(
        selectedTab: BalanceTabOptions,
    ) {
        setUiState(
            shownTab = selectedTab,
        )
    }
}

enum class TimeFilterForSegmentedButton(
    val textId: Int,
    val dateFormat: String,
) {
    Week(
        textId = R.string.week,
        dateFormat = "EE, dd MMM",
    ),
    Month(
        textId = R.string.month,
        dateFormat = "MMMM yyyy",
    ),
    Year(
        textId = R.string.year,
        dateFormat = "yyyy",
    ),
    All(
        textId = R.string.all,
        dateFormat = "dd/MM/yyyy"
    );

    fun getStartDate(): Date {

        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = TimeTools.timeSetBeginningOfDay(Date())

        when(this) {

            Week -> startDateCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
            Month -> startDateCalendar.set(Calendar.DAY_OF_MONTH,1)
            Year -> startDateCalendar.set(Calendar.DAY_OF_YEAR,1)
            All -> {
                val epochTime = Date()
                epochTime.time = 0L
                startDateCalendar.time = epochTime
            }
        }

        return startDateCalendar.time
    }
    fun getEndDate(): Date {

        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = Date()

        endDateCalendar.set(
            Calendar.HOUR_OF_DAY,23
        )
        endDateCalendar.set(
            Calendar.MINUTE,59
        )
        endDateCalendar.set(
            Calendar.SECOND,59
        )
        endDateCalendar.set(
            Calendar.MILLISECOND,999
        )

        when(this) {

            Week -> endDateCalendar.set(
                Calendar.DAY_OF_WEEK,
                Calendar.SUNDAY
            )
            Month -> endDateCalendar.set(
                Calendar.DAY_OF_MONTH,
                endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            Year -> endDateCalendar.set(
                Calendar.DAY_OF_YEAR,
                endDateCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)
            )
            All -> { }
        }

        return endDateCalendar.time
    }
}