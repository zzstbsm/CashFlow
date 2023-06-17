package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*


data class WalletEditUiState(
    val wallet: Wallet = Wallet.emptyWallet(),
    val budgetPeriod: BudgetPeriod = BudgetPeriod(),
) {
    fun updateWalletName(
        name : String,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                name = name
            )
        )
    }

    fun updateWalletAmount(
        amount: Float,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                startAmount = amount
            )
        )
    }

    fun updateWalletCreationDate(
        creationDate: Date,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                creationDate = creationDate
            )
        )
    }

    fun updateWalletBudgetEnabled(
        budgetEnabled: Boolean
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                budgetEnabled = budgetEnabled
            )
        )
    }

    fun updateWalletBudgetStartDate(
        creationDate: Date,
    ) : WalletEditUiState{
        return this.copy(
            budgetPeriod = this.budgetPeriod.copy(
                startDate = creationDate
            )
        )
    }

    fun updateWalletBudgetEndDate(
        creationDate: Date,
    ) : WalletEditUiState{
        return this.copy(
            budgetPeriod = this.budgetPeriod.copy(
                endDate = creationDate
            )
        )
    }

}

class WalletEditViewModel(
    navController: NavController,
): ViewModel() {

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {



        viewModelScope.launch(Dispatchers.IO){
        }
    }

    fun updateWalletName(name: String) {
        _uiState.value = uiState.value.updateWalletName(name)
    }

    fun updateWalletAmount(amount: Float) {
        _uiState.value = uiState.value.updateWalletAmount(amount)
    }

    fun updateWalletCreationDate(millis: Long?) {
        if (millis != null) {
            _uiState.value = uiState.value.updateWalletCreationDate(Date(millis))
        }
    }

    fun updateWalletBudgetStartDate(millis: Long?) {
        if (millis != null) {
            _uiState.value = uiState.value.updateWalletBudgetStartDate(Date(millis))
        }
    }

    fun updateWalletBudgetEndDate(millis: Long?) {
        if (millis != null) {
            _uiState.value = uiState.value.updateWalletBudgetEndDate(Date(millis))
        }
    }

    fun updateWalletBudgetEnabled(budgetEnabled: Boolean) {
        _uiState.value = uiState.value.updateWalletBudgetEnabled(budgetEnabled = budgetEnabled)
    }

}