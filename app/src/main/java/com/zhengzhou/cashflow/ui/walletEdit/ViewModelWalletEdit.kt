package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import com.zhengzhou.cashflow.tools.calculator.Calculator
import com.zhengzhou.cashflow.tools.calculator.mapCharToKeypadDigit
import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID



class WalletEditViewModel(
    walletUUID: UUID,
): ViewModel() {

    private var _newWallet = walletUUID == UUID(0L,0L)
    private var _wallet = MutableStateFlow(Wallet.loadingWallet())
    val wallet: StateFlow<Wallet> = _wallet.asStateFlow()

    private var _budgetEnabledWhenLoaded: Boolean = false

    private var writingOnUiState: Boolean = false

    private var _walletListName = MutableStateFlow(listOf<String>())
    private val walletListName: StateFlow<List<String>> = _walletListName.asStateFlow()

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var calculator: Calculator = Calculator()

    private var jobLoadWallet: Job
    private var jobLoadWalletListName: Job

    init {

        _newWallet = walletUUID == UUID(0L,0L)

        // Load wallet
        jobLoadWallet = loadWallet(walletUUID = walletUUID)

        jobLoadWalletListName = viewModelScope.launch {
            repository.getWalletListOfNames().collect {
                _walletListName.value = it
            }
        }
    }

    private fun setUiState(
        isLoading: Boolean? = null,

        amountOnScreen: String? = null,
        isErrorAmountOnScreen: Boolean? = null,

        isErrorWalletNameInUse: Boolean? = null,
        isErrorWalletNameNotValid: Boolean? = null,

    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)
            writingOnUiState = true

            _uiState.value = WalletEditUiState(
                isLoading = isLoading ?: uiState.value.isLoading,

                amountOnScreen = amountOnScreen ?: uiState.value.amountOnScreen,
                isErrorAmountOnScreen = isErrorAmountOnScreen ?: uiState.value.isErrorAmountOnScreen,

                isErrorWalletNameInUse = isErrorWalletNameInUse ?: uiState.value.isErrorWalletNameInUse,
                isErrorWalletNameNotValid = isErrorWalletNameNotValid ?: uiState.value.isErrorWalletNameNotValid,

            )

            writingOnUiState = false
        }
    }

    private fun loadWallet(walletUUID: UUID): Job {
        return viewModelScope.launch(Dispatchers.IO) {

            _wallet.value = if (_newWallet) {
                Wallet.newEmpty()
            } else {
                repository.getWallet(walletUUID) ?: Wallet.newEmpty()
            }

            calculator = Calculator.initialize(wallet.value.startAmount)
            updateAmountOnScreen(amount = wallet.value.startAmount.toString())

        }
    }

    fun updateAmountOnScreen(amount: String) {

        var validAmount = true

        amount.forEach { digit ->
            val key = mapCharToKeypadDigit(digit)
            if (key == null) {
                validAmount = false
                return@forEach
            }
        }

        setUiState(
            amountOnScreen = amount,
            isErrorAmountOnScreen = !validAmount
        )
    }

    fun updateWallet(
        name: String? = null,
        startAmount: Float? = null,
        iconName: IconsMappedForDB? = null,
        currency: Currency? = null,
        creationDate: Date? = null,
        lastAccess: Date? = null,
        budgetEnabled: Boolean? = null,
    ) {
        val newWalletForUi = wallet.value.copy(
            name = name ?: wallet.value.name,
            startAmount = startAmount ?: wallet.value.startAmount,
            iconName = iconName ?: wallet.value.iconName,
            currency = currency ?: wallet.value.currency,
            creationDate = creationDate ?: wallet.value.creationDate,
            lastAccess = lastAccess ?: wallet.value.lastAccess,
            budgetEnabled = budgetEnabled ?: wallet.value.budgetEnabled,
        )

        val isErrorWalletNameInUse = if (name == null) uiState.value.isErrorWalletNameInUse else {
            val checkedName = removeSpaceFromStringEnd(name)
            checkedName in walletListName.value
        }
        val isErrorWalletNameNotValid = if (name == null) uiState.value.isErrorWalletNameNotValid else {
            val checkedName = removeSpaceFromStringEnd(name)
            checkedName.isEmpty()
        }

        setUiState(
            isErrorWalletNameInUse = isErrorWalletNameInUse,
            isErrorWalletNameNotValid = isErrorWalletNameNotValid,

            wallet = newWalletForUi
        )
    }

    fun saveWallet(): WalletEditSaveResults {

        if (uiState.value.isErrorWalletNameInUse) {
            return WalletEditSaveResults.NAME_IN_USE
        } else if (uiState.value.isErrorWalletNameNotValid) {
            return WalletEditSaveResults.NON_VALID_NAME
        } else if (uiState.value.isErrorAmountOnScreen) {
            return WalletEditSaveResults.NON_VALID_AMOUNT
        }

        viewModelScope.launch {

            val amount = Calculator.initialize(uiState.value.amountOnScreen).getAmount()

            // Save wallet
            var wallet = uiState.value.wallet.copy(startAmount = amount)
            while (wallet.name.last() == ' ') {
                wallet = wallet.copy(
                    name = wallet.name.dropLast(1)
                )
            }
            if (_newWallet) {
                repository.addWallet(wallet)
            } else {
                repository.updateWallet(wallet)
            }
        }
        return WalletEditSaveResults.SUCCESS
    }
}
