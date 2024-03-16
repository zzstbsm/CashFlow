package com.zhengzhou.cashflow.wallet_edit.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.tools.calculator.Calculator
import com.zhengzhou.cashflow.tools.calculator.mapCharToKeypadDigit
import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
import com.zhengzhou.cashflow.wallet_edit.WalletEditSaveResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

internal class WalletEditViewModel(
    repository: RepositoryInterface,
    walletUUID: UUID,
): ViewModel() {

    private val walletUseCases = WalletUseCases(repository)

    private var _newWallet = walletUUID == UUID(0L,0L)

    private var _budgetEnabledWhenLoaded: Boolean = false

    private var writingOnUiState: Boolean = false

    private var _walletListName = MutableStateFlow(listOf<String>())
    private val walletListName: StateFlow<List<String>> = _walletListName.asStateFlow()

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private var calculator: Calculator = Calculator()

    private var jobLoadWallet: Job
    private var jobLoadWalletListName: Job

    init {

        _newWallet = walletUUID == UUID(0L,0L)

        // Load wallet
        jobLoadWallet = loadWallet(walletUUID = walletUUID)

        jobLoadWalletListName = viewModelScope.launch {
            walletUseCases.getListOfNames().collect {
                _walletListName.value = it
            }
        }
    }

    private fun setUiState(

        wallet: Wallet? = null,

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
                wallet = wallet ?: uiState.value.wallet,

                isLoading = isLoading ?: uiState.value.isLoading,

                amountOnScreen = amountOnScreen ?: uiState.value.amountOnScreen,
                isErrorAmountOnScreen = isErrorAmountOnScreen ?: uiState.value.isErrorAmountOnScreen,

                isErrorWalletNameInUse = isErrorWalletNameInUse ?: uiState.value.isErrorWalletNameInUse,
                isErrorWalletNameNotValid = isErrorWalletNameNotValid ?: uiState.value.isErrorWalletNameNotValid,

            )

            writingOnUiState = false
        }
    }

    fun onEvent(event: WalletEditEvent) {
        when (event) {
            is WalletEditEvent.UpdateAmount -> {

                val amount = event.amount

                // Ignore the strings with more than 1 dot
                if (amount.count { it == '.' } > 1) return

                // Ignore all the strings with more than 2 digits after the dot
                if (amount.count { it == '.' } == 1) {
                    val decimals = amount.split('.')[1]
                    if (decimals.length > 2) return
                }

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
            is WalletEditEvent.UpdateWallet -> {

                val wallet = uiState.value.wallet

                val newWalletForUi = wallet.copy(
                    name = event.name ?: wallet.name,
                    startAmount = event.startAmount ?: wallet.startAmount,
                    iconName = event.iconName ?: wallet.iconName,
                    currency = event.currency ?: wallet.currency,
                    creationDate = event.creationDate ?: wallet.creationDate,
                    lastAccess = event.lastAccess ?: wallet.lastAccess,
                    budgetEnabled = event.budgetEnabled ?: wallet.budgetEnabled,
                )

                val isErrorWalletNameInUse = if (event.name == null) uiState.value.isErrorWalletNameInUse else {
                    val checkedName = removeSpaceFromStringEnd(event.name)
                    checkedName in walletListName.value
                }
                val isErrorWalletNameNotValid = if (event.name == null) uiState.value.isErrorWalletNameNotValid else {
                    val checkedName = removeSpaceFromStringEnd(event.name)
                    checkedName.isEmpty()
                }

                setUiState(
                    isErrorWalletNameInUse = isErrorWalletNameInUse,
                    isErrorWalletNameNotValid = isErrorWalletNameNotValid,

                    wallet = newWalletForUi
                )
            }
        }
    }

    private fun loadWallet(walletUUID: UUID): Job {
        return viewModelScope.launch(Dispatchers.IO) {

            val wallet = if (_newWallet) {
                Wallet.newEmpty()
            } else {
                walletUseCases.getWallet(walletUUID) ?: Wallet.newEmpty()
            }

            calculator = Calculator.initialize(wallet.startAmount)

            onEvent(
                WalletEditEvent.UpdateAmount(amount = wallet.startAmount.toString())
            )

            setUiState(
                isLoading = false,
                wallet = wallet,
            )

        }
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
                walletUseCases.addWallet(wallet)
            } else {
                walletUseCases.updateWallet(wallet)
            }
        }
        return WalletEditSaveResults.SUCCESS
    }
}
