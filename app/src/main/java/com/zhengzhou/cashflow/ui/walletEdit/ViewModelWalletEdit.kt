package com.zhengzhou.cashflow.ui.walletEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.BudgetPeriod
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.mapCharToKeypadDigit
import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*


data class WalletEditUiState(
    val isLoading: Boolean = true,

    val amountOnScreen: String = "",
    val isErrorAmountOnScreen: Boolean = false,

    val isErrorWalletNameInUse: Boolean = false,
    val isErrorWalletNameNotValid: Boolean = false,

    val wallet: Wallet = Wallet.loadingWallet(),
    val budgetPeriod: BudgetPeriod = BudgetPeriod(),
    val groupCategoryAndBudgetList: List<GroupCategoryAndBudget> = listOf(),
) {
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

    private var writingOnUiState: Boolean = false

    private var _walletListName = MutableStateFlow(listOf<String>())
    val walletListName: StateFlow<List<String>> = _walletListName.asStateFlow()

    private var _uiState = MutableStateFlow(WalletEditUiState())
    val uiState: StateFlow<WalletEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var calculator: Calculator = Calculator()

    private var jobLoadBudget: Job
    private var jobLoadWallet: Job
    private var jobLoadWalletListName: Job

    init {

        _newWallet = walletUUID == UUID(0L,0L)

        // Load wallet
        jobLoadWallet = loadWallet(walletUUID = walletUUID)
        jobLoadBudget = loadBudget(walletUUID = walletUUID)

        jobLoadWalletListName = viewModelScope.launch {
            repository.getWalletListOfNames().collect() {
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
        budgetPeriod: BudgetPeriod? = null,
        groupCategoryAndBudgetList: List<GroupCategoryAndBudget>? = null,
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
                budgetPeriod = budgetPeriod ?: uiState.value.budgetPeriod,
                groupCategoryAndBudgetList = groupCategoryAndBudgetList ?: uiState.value.groupCategoryAndBudgetList
            )

            writingOnUiState = false
        }
    }

    private fun loadBudget(walletUUID: UUID): Job {
        return viewModelScope.launch(Dispatchers.IO) {

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
    }

    private fun loadWallet(walletUUID: UUID): Job {
        return viewModelScope.launch(Dispatchers.IO) {


            val retrievedWallet = if (_newWallet) {
                Wallet()
            } else {
                repository.getWallet(walletUUID) ?: Wallet()
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
        iconName: String? = null,
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

    fun saveWallet(): WalletEditSaveResults {

        if (uiState.value.isErrorWalletNameInUse) {
            EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_already_in_use)
            return WalletEditSaveResults.NAME_IN_USE
        } else if (uiState.value.isErrorWalletNameNotValid) {
            EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_not_valid)
            return WalletEditSaveResults.NON_VALID_NAME
        } else if (uiState.value.isErrorAmountOnScreen) {
            EventMessages.sendMessageId(R.string.WalletEdit_error_amount_non_valid)
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
        return WalletEditSaveResults.SUCCESS
    }
}

enum class WalletEditSaveResults(
    val message: Int,

) {
    SUCCESS(
        message = R.string.WalletEdit_wallet_saved
    ),
    NON_VALID_NAME(
        message = R.string.WalletEdit_error_wallet_name_not_valid
    ),
    NON_VALID_AMOUNT(
        message = R.string.WalletEdit_error_amount_non_valid
    ),
    NAME_IN_USE(
        message = R.string.WalletEdit_error_wallet_name_already_in_use
    )
}