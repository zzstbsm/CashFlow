package com.zhengzhou.cashflow.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.*
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

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

private fun setCurrencyFormatter(currencyString: String) : NumberFormat {

    val currency: Currency? = setCurrency(currencyString)

    return if (currency != null) {
        NumberFormat.getCurrencyInstance(currency.locale)
    } else {
        NumberFormat.getCurrencyInstance()
    }
}


data class BalanceGroup(
    var name: String = "Group",
    var groupId: UUID = UUID.randomUUID(),
    var iconId: Int = R.drawable.ic_wallet,
    var walletSelection: List<WalletSelection> = listOf(WalletSelection()),
    val transactionList: List<Transaction> = listOf(),
    var lastAccess: Date = Date(),
    val currencyFormatter: NumberFormat = setCurrencyFormatter(Currency.EUR.abbreviation),
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