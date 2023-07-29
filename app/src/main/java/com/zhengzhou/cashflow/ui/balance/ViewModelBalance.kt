package com.zhengzhou.cashflow.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.*
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class BalanceUiState(
    val isLoading: Boolean = true,
    val equivalentWallet: Wallet = Wallet(
        id = UUID(0L,0L),
        name = "All wallets",
        currency = Currency.EUR,
    ),
    val walletList: List<Wallet> = listOf(),
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

class BalanceViewModel() : ViewModel() {

    private val repository = DatabaseRepository.get()

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private var writingOnUiState: Boolean = false

    private var jobGetTransactionList: Job

    init {

        viewModelScope.launch(Dispatchers.IO) {
            // Collect all wallets
            repository.getWalletList().collect { collectedWalletList ->
                setUiState(
                    walletList = collectedWalletList,
                    isLoading = false
                )
            }
        }

        jobGetTransactionList = getTransactionList()

    }

    private fun setUiState(
        isLoading: Boolean? = null,

        equivalentWallet: Wallet? = null,

        walletList: List<Wallet>? = null,

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

                transactionList = transactionList ?: uiState.value.transactionList,
                transactionListToShow = transactionListToShow ?: uiState.value.transactionListToShow,

                filterStartDate = filterStartDate ?: uiState.value.filterStartDate,
                filterEndDate = filterEndDate ?: uiState.value.filterEndDate,
                timeFilter = timeFilterValue,
            )

            writingOnUiState = false
        }
    }

    private fun getTransactionList(): Job {
        return viewModelScope.launch {
            while (uiState.value.isLoading) delay(5)

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

    fun setTimeFilter(
        timeFilter: TimeFilterForSegmentedButton?,
        startDate: Date = Date(),
        endDate: Date = Date(),
    ) {

        val toSaveStartDate: Date
        val toSaveEndDate: Date

        if (timeFilter == null) {
            toSaveStartDate = startDate
            toSaveEndDate = endDate
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

    private fun getFilteredTransactionList(
        startDate: Date = uiState.value.filterStartDate,
        endDate: Date = uiState.value.filterEndDate,
    ): List<TransactionAndCategory> {
        return uiState.value.transactionList.filter { transactionAndCategory ->

            transactionAndCategory.transaction.date in startDate..endDate

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
        startDateCalendar.time = Date()

        startDateCalendar.set(
            Calendar.HOUR_OF_DAY,0
        )
        startDateCalendar.set(
            Calendar.MINUTE,0
        )
        startDateCalendar.set(
            Calendar.SECOND,0
        )
        startDateCalendar.set(
            Calendar.MILLISECOND,0
        )

        when(this) {

            Week -> startDateCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
            Month -> startDateCalendar.set(Calendar.DAY_OF_MONTH,1)
            Year -> startDateCalendar.set(Calendar.DAY_OF_YEAR,1)
            All -> {
                val zeroTime = Date()
                zeroTime.time = 0L
                startDateCalendar.time = zeroTime
            }
        }

        return startDateCalendar.time
    }
    fun getEndDate(): Date {

        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = Date()

        endDateCalendar.set(
            Calendar.HOUR_OF_DAY,0
        )
        endDateCalendar.set(
            Calendar.MINUTE,0
        )
        endDateCalendar.set(
            Calendar.SECOND,0
        )
        endDateCalendar.set(
            Calendar.MILLISECOND,0
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

/*
data class BalanceUiState(
    val balanceGroup: BalanceGroup = BalanceGroup(),
    val equivalentWallet: Wallet = Wallet()
) {

    suspend fun selectWalletInGroup(walletPositionInArray: Int) : BalanceUiState {

        val tempGroup = this.balanceGroup.checkSingleWalletInGroup(walletPositionInArray = walletPositionInArray)
        return this.copy(
            balanceGroup = tempGroup
        )
    }

    suspend fun setWalletList(walletList: List<Wallet>) : BalanceUiState {

        val tempGroup = BalanceGroup()
        val tempWalletSelection: MutableList<WalletSelection> = mutableListOf()
        for (wallet in walletList) {
            tempWalletSelection += WalletSelection(wallet = wallet, toShow = true) // TODO: set toShow based on the config file
        }
        tempGroup.setWalletsInGroup(tempWalletSelection)
        tempGroup.refreshTransactionList()

        return this.copy(
            balanceGroup = tempGroup
        )
    }
}

data class BalanceGroup(
    var name: String = "All wallets",
    var groupId: UUID = UUID.randomUUID(),
    var iconId: Int = R.drawable.ic_wallet,
    var walletSelection: List<WalletSelection> = listOf(WalletSelection()),
    val transactionList: List<Transaction> = listOf(),
    var lastAccess: Date = Date(),
    val currencyFormatter: NumberFormat = Currency.setCurrencyFormatter(Currency.EUR.abbreviation),
) {
    suspend fun checkSingleWalletInGroup(
        walletPositionInArray: Int
    ) : BalanceGroup {
        val tempBalanceGroup = this.copy()

        tempBalanceGroup.walletSelection[walletPositionInArray].invertToShow()
        tempBalanceGroup.refreshTransactionList()

        return tempBalanceGroup
    }

    suspend fun refreshTransactionList(): BalanceGroup {
        val tempTransactionList = mutableListOf<Transaction>()
        if (walletSelection.isEmpty()) {
            return this.copy(
                name = "No wallets",
                lastAccess = Date(),
                transactionList = listOf(),
            )
        }

        walletSelection.forEach { walletSelection ->
            if (walletSelection.toShow)
                tempTransactionList += getTransactionList(walletSelection.wallet.id)
        }
        return this.copy(
            transactionList = tempTransactionList,
            lastAccess = Date(),
        )
    }

    fun setWalletsInGroup(listOfWalletSelection: List<WalletSelection>) {
        this.walletSelection = listOfWalletSelection
    }

    private suspend fun getTransactionList(walletId: UUID) : List<Transaction> {
        val repository = DatabaseRepository.get()
        var transactionList = listOf<Transaction>()
        repository.getTransactionListInWallet(walletId).collect { transactionListPerWallet ->
            transactionList = transactionListPerWallet
        }
        return transactionList
    }

    fun getGroupInitialBalance() : Float {
        var tempTotalInWallets = 0f
        walletSelection.forEach { walletSelection ->
            if (walletSelection.toShow)
                tempTotalInWallets += walletSelection.wallet.startAmount
        }
        return tempTotalInWallets
    }

    private fun updateEquivalentWallet() {
        // TODO
    }

}

class BalanceViewModel(
    navController: NavController,
) : ViewModel() {

    private val repository = DatabaseRepository.get()

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private var _navController: NavController

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWalletList().collect { walletList ->
                _uiState.value = uiState.value.setWalletList(walletList)
            }
        }
        _navController = navController
    }

    fun walletGroupBalance(): Float {
        return uiState.value.balanceGroup.getGroupInitialBalance()
    }

    fun getCurrencyFormatter() : NumberFormat {
        return uiState.value.balanceGroup.currencyFormatter
    }
    fun getTransactionList() : List<Transaction> {
        return uiState.value.balanceGroup.transactionList
    }
}

 */