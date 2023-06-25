package com.zhengzhou.cashflow.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.*
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

data class BalanceUiState(
    val isLoading: Boolean = true,
    val equivalentWallet: Wallet = Wallet(
        id = UUID(0L,0L),
        name = "All wallets with Euro",
        currency = Currency.EUR
    ),
    val walletList: List<Wallet> = listOf(),
    val transactionList: List<Transaction> = listOf(),
) {

    fun getBalance(): Float {

        var amount: Float = this.updateEquivalentWallet().equivalentWallet.startAmount

        this.transactionList.forEach { transaction ->
            val transactionType = TransactionType.setTransaction(transaction.movementType)
            amount += when (transactionType) {
                TransactionType.Deposit -> transaction.amount
                TransactionType.Expense -> -transaction.amount
                else -> 0f
            }
        }
        return amount
    }

    fun getLastWallet(): Wallet {
        return this.walletList.maxByOrNull { wallet ->
            wallet.lastAccess
        } ?: Wallet()
    }

    private fun updateEquivalentWallet() : BalanceUiState{

        var startAmount: Float = 0f

        this.walletList.forEach { wallet ->
            startAmount += wallet.startAmount
        }

        return this.copy(
            equivalentWallet = this.equivalentWallet.copy(
                startAmount = startAmount,
            )
        )

    }

    suspend fun updateTransactionList() : BalanceUiState {

        val transactionList: MutableList<Transaction> = mutableListOf()

        val repository = DatabaseRepository.get()
        this.walletList.forEach { wallet ->
            repository.getTransactionListInWallet(wallet.id).collect { collectedTransactionList ->
                transactionList  += collectedTransactionList
            }
        }

        return this.copy(
            transactionList = transactionList
        )

    }

}

class BalanceViewModel() : ViewModel() {

    private val repository = DatabaseRepository.get()

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    init {

        var isLoadingWallets = true
        var walletList: List<Wallet> = listOf()

        viewModelScope.launch(Dispatchers.IO) {
            // Collect all wallets
            repository.getWalletList().collect { collectedWalletList ->
                walletList = collectedWalletList
                isLoadingWallets = false
            }
        }

        viewModelScope.launch {
            while (isLoadingWallets) {
                delay(20)
            }
            _uiState.value = uiState.value.updateTransactionList()

        }

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