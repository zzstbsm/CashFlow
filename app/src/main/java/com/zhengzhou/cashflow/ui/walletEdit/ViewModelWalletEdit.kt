package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.mapCharToKeypadDigit
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

data class WalletEditUiState(
    val isLoading: Boolean = true,

    val amountOnScreen: String = "",
    val isErrorAmountOnScreen: Boolean = false,

    val isErrorWalletNameInUse: Boolean = false,
    val isErrorWalletNameNotValid: Boolean = false,

    val wallet: Wallet = Wallet.loadingWallet(),
)

class WalletEditViewModel(
    walletUUID: UUID,
): ViewModel() {

    private var _newWallet = true
    private var _budgetEnabledWhenLoaded: Boolean = false

    private var writingOnUiState: Boolean = false

    private var _walletListName = MutableStateFlow(listOf<String>())
    val walletListName: StateFlow<List<String>> = _walletListName.asStateFlow()

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

        wallet: Wallet? = null,
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

                wallet = wallet ?: uiState.value.wallet,
            )

            writingOnUiState = false
        }
    }

    private fun loadWallet(walletUUID: UUID): Job {
        return viewModelScope.launch(Dispatchers.IO) {


            val retrievedWallet = if (_newWallet) {
                Wallet.newEmpty()
            } else {
                repository.getWallet(walletUUID) ?: Wallet.newEmpty()
            }

            setUiState(
                wallet = retrievedWallet,
                isLoading = false,
            )
            _budgetEnabledWhenLoaded = uiState.value.wallet.budgetEnabled

            calculator = Calculator.initialize(uiState.value.wallet.startAmount)
            updateAmountOnScreen(amount = retrievedWallet.startAmount.toString())

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
        iconName: com.zhengzhou.cashflow.themes.IconsMappedForDB? = null,
        currency: Currency? = null,
        creationDate: Date? = null,
        lastAccess: Date? = null,
        budgetEnabled: Boolean? = null,
    ) {
        val wallet = uiState.value.wallet
        val newWalletForUi = wallet.copy(
            name = name ?: wallet.name,
            startAmount = startAmount ?: wallet.startAmount,
            iconName = iconName ?: wallet.iconName,
            currency = currency ?: wallet.currency,
            creationDate = creationDate ?: wallet.creationDate,
            lastAccess = lastAccess ?: wallet.lastAccess,
            budgetEnabled = budgetEnabled ?: wallet.budgetEnabled,
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
