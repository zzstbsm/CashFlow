package com.zhengzhou.cashflow.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.*
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.timeSetBeginningOfDay
import com.zhengzhou.cashflow.tools.timeSetEndOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

data class BalanceUiState(
    val isLoading: Boolean = true,
    val equivalentWallet: Wallet = Wallet(
        id = UUID(0L,0L),
        name = "All wallets",
        currency = Currency.EUR,
    ),
    val walletList: List<Wallet> = listOf(),
    val currencyList: List<Currency> = listOf(),
    val transactionList: List<TransactionAndCategory> = listOf(),
    val transactionListToShow: List<TransactionAndCategory> = listOf(),

    val filterStartDate: Date = TimeFilterForSegmentedButton.Month.getStartDate(),
    val filterEndDate: Date = TimeFilterForSegmentedButton.Month.getEndDate(),
    val timeFilter: TimeFilterForSegmentedButton? = TimeFilterForSegmentedButton.Month,

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
        } ?: Wallet()
    }

    private fun updateEquivalentWallet() : BalanceUiState{

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

class BalanceViewModel : ViewModel() {

    private val repository = DatabaseRepository.get()

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private var currencyFormatter: NumberFormat

    private var writingOnUiState: Boolean = false

    private var jobGetCurrency: Job
    private var jobGetTransactionList: Job
    private var jobGetWalletList: Job

    init {

        jobGetCurrency = getCurrencyList()
        jobGetWalletList = getWalletList(uiState.value.equivalentWallet.currency)
        currencyFormatter = Currency.setCurrencyFormatter(uiState.value.equivalentWallet.currency.abbreviation)
        jobGetTransactionList = getTransactionList()

    }

    private fun setUiState(
        isLoading: Boolean? = null,

        equivalentWallet: Wallet? = null,

        walletList: List<Wallet>? = null,
        currencyList: List<Currency>? = null,

        transactionList: List<TransactionAndCategory>? = null,
        transactionListToShow: List<TransactionAndCategory>? = null,

        filterStartDate: Date? = null,
        filterEndDate: Date? = null,
        timeFilter: TimeFilterForSegmentedButton? = null,
        setTimeFilter: Boolean = false
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

                transactionList = transactionList ?: uiState.value.transactionList,
                transactionListToShow = transactionListToShow ?: uiState.value.transactionListToShow,

                filterStartDate = filterStartDate ?: uiState.value.filterStartDate,
                filterEndDate = filterEndDate ?: uiState.value.filterEndDate,
                timeFilter = timeFilterValue,
            )

            writingOnUiState = false
        }
    }


    fun getCurrencyFormatter(): NumberFormat = currencyFormatter
    private fun getCurrencyList(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            // Collect all wallets
            repository.getWalletCurrencyList().collect { currencyList ->
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

            repository.getTransactionListInListOfWallet(uiState.value.walletList).collect { transactionList ->

                val transactionCategoryGroup: MutableList<TransactionAndCategory> = mutableListOf()

                transactionList.forEach { transaction ->
                    val category = repository.getCategory(transaction.idCategory) ?: Category()
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
            currencyFormatter = Currency.setCurrencyFormatter(currency.abbreviation)
            setUiState(
                isLoading = true
            )
            // Collect all wallets
            repository.getWalletListByCurrency(currency).collect { collectedWalletList ->
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
        startDate: Date = Date(),
        endDate: Date = Date(),
    ) {

        val toSaveStartDate: Date
        val toSaveEndDate: Date

        if (timeFilter == null) {
            toSaveStartDate = timeSetBeginningOfDay(startDate)
            toSaveEndDate = timeSetEndOfDay(endDate)
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
}

enum class TimeFilterForSegmentedButton(
    val textId: Int,
) {
    Week(
        textId = R.string.Balance_week,
    ),
    Month(
        textId = R.string.Balance_month
    ),
    Year(
        textId = R.string.Balance_year
    ),
    All(
        textId = R.string.Balance_all
    );

    fun getStartDate(): Date {

        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = timeSetBeginningOfDay(Date())

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