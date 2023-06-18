package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.ConfigurationFirstStartup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates


data class WalletEditUiState(
    val wallet: Wallet = Wallet.emptyWallet(),
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
        currency: String,
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
    navController: NavController,
): ViewModel() {

    var newWallet = true

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {

        newWallet = walletUUID == UUID(0L,0L)

        viewModelScope.launch(Dispatchers.IO) {

            _uiState.value = uiState.value.copy(
                wallet = if (newWallet) {
                    Wallet.emptyWallet()
                } else {
                    repository.getWallet(walletUUID) ?: Wallet.emptyWallet()
                }
            )
        }

        viewModelScope.launch(Dispatchers.IO) {

            // Load data about the budget period
            if (!newWallet) {
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

                    val budgetCategory: BudgetCategory = if (newWallet) {
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

    fun updateWalletCurrency(currency: Currency) {
        _uiState.value = uiState.value.updateWalletCurrency(
            currency = currency.abbreviation
        )
    }
}