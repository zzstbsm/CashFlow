package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.ConfigurationFirstStartup
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.KeypadDigit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


data class WalletEditUiState(
    val isLoading: Boolean = true,
    val isErrorNameOfWallet: Boolean = false,
    val wallet: Wallet = Wallet.loadingWallet(),
    val budgetPeriod: BudgetPeriod = BudgetPeriod(),
    val groupCategoryAndBudgetList: List<GroupCategoryAndBudget> = listOf(),
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
    ) : WalletEditUiState {
        return this.copy(
            wallet = this.wallet.copy(
                budgetEnabled = budgetEnabled
            ),
            budgetPeriod = this.budgetPeriod.copy(
                id = UUID.randomUUID(),
                walletId = this.wallet.id,
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

    fun updateWalletCurrency(
        currency: Currency,
    ) : WalletEditUiState{
        return this.copy(
            wallet = this.wallet.copy(
                currency = currency
            )
        )
    }
}

data class GroupCategoryAndBudget(
    val budgetCategory: BudgetCategory = BudgetCategory(),
    val category: Category = Category(),
)

class WalletEditViewModel(
    walletUUID: UUID,
): ViewModel() {

    private var _newWallet = true
    private var _budgetEnabledWhenLoaded: Boolean = false

    private var _walletListName = MutableStateFlow(listOf<String>())
    val walletListName: StateFlow<List<String>> = _walletListName.asStateFlow()

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var calculator: Calculator = Calculator()

    private var jobLoadBudget: Job
    private var jobLoadWalletListName: Job

    init {

        _newWallet = walletUUID == UUID(0L,0L)

        // Load wallet
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.value = uiState.value.copy(
                wallet = if (_newWallet) {
                    Wallet()
                } else {
                    repository.getWallet(walletUUID) ?: Wallet()
                },
                isLoading = false,
            )
            _budgetEnabledWhenLoaded = uiState.value.wallet.budgetEnabled

            calculator = Calculator.initialize(uiState.value.wallet.startAmount)

        }

        jobLoadBudget = viewModelScope.launch(Dispatchers.IO) {

            // Load data about the budget period
            if (!_newWallet) {
                _uiState.value = uiState.value.copy(
                    budgetPeriod = repository.getBudgetPeriodLastActive(
                        walletUUID = walletUUID
                    ) ?: BudgetPeriod()
                )
            }

            // Handle the initialization of the budget per category
            repository.getCategoryList().collect { categoryList ->

                if (categoryList.isEmpty()) {
                    // TODO: should be removed in production since it should be handled somewhere else
                    ConfigurationFirstStartup.configureTableCategory()
                }
                val categoryListExpenses = categoryList.filter {category ->
                    category.transactionTypeId == TransactionType.Expense.id
                }

                val groupCategoryAndBudgetList: MutableList<GroupCategoryAndBudget> = mutableListOf()
                categoryListExpenses.forEach {category ->

                    val budgetCategory: BudgetCategory = if (_newWallet) {
                        BudgetCategory.newCategory(
                            idCategory = category.id
                        )
                    } else {
                        if (uiState.value.wallet.budgetEnabled) {
                            repository.getBudgetCategory(
                                budgetPeriodUUID = uiState.value.budgetPeriod.id,
                                categoryUUID = category.id,
                            ) ?: BudgetCategory()
                        } else {
                            BudgetCategory()
                        }

                    }

                    groupCategoryAndBudgetList.add(
                        GroupCategoryAndBudget(
                            budgetCategory = budgetCategory,
                            category = category,
                        )
                    )
                }

                _uiState.value = uiState.value.copy(
                    groupCategoryAndBudgetList = groupCategoryAndBudgetList
                )
            }
        }
        jobLoadWalletListName = viewModelScope.launch {
            repository.getWalletListOfNames().collect() {
                _walletListName.value = it
            }
        }
    }

    fun getOnScreenString() : String {
        return calculator.onScreenString()
    }

    fun onKeyPressed(key: KeypadDigit) {
        if (key == KeypadDigit.KeyBack) {
            calculator.dropLastDigit()
        } else {
            calculator.addKey(key)
        }

        updateWalletAmount(calculator.onScreenString().toFloat())

    }

    fun updateWalletName(name: String) {

        val checkedName = if (name.isNotEmpty() && name.last() == ' ') name.dropLast(1) else name
        _uiState.value = uiState.value.copy(
            isErrorNameOfWallet = checkedName in walletListName.value
        )
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

    fun updateWalletCurrency(currency: Currency) {
        _uiState.value = uiState.value.updateWalletCurrency(
            currency = currency
        )
    }

    fun saveWallet() {

        if (uiState.value.isErrorNameOfWallet) {
            EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_already_in_use)
            return
        }

        viewModelScope.launch {

            // Save wallet
            var wallet = uiState.value.wallet
            if (wallet.name.last() == ' ') {
                wallet = wallet.copy(
                    name = wallet.name.dropLast(1)
                )
            }
            if (_newWallet) {
                repository.addWallet(wallet)
            } else {
                repository.updateWallet(wallet)
            }

            // Currently (23/06/2023) disabled
            val budgetEnabledWhileSaving = uiState.value.wallet.budgetEnabled
            if (_budgetEnabledWhenLoaded != budgetEnabledWhileSaving) {

                if (budgetEnabledWhileSaving && _newWallet) {
                    repository.addBudgetPeriod(uiState.value.budgetPeriod)
                } else {
                    repository.updateBudgetPeriod(uiState.value.budgetPeriod)
                }

                // TODO: handle budget and category while saving the budget in database.
                uiState.value.groupCategoryAndBudgetList.forEach { groupCategoryAndBudget ->
                    val category = groupCategoryAndBudget.category
                    val budgetCategory = groupCategoryAndBudget.budgetCategory

                    // Actions

                }
            }
            EventMessages.sendMessageId(R.string.WalletEdit_wallet_saved)
        }
    }
}