package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.KeypadDigit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

data class TransactionEditUiState(
    val isLoading: Boolean = true,
    val amountString: String = "0",
    val wallet: Wallet = Wallet(),
    val transaction: Transaction = Transaction(),
    val walletList: List<Wallet> = listOf(),
    val categoryList: List<Category> = listOf(),
    val tagListInDB: List<Tag> = listOf(),
    val transactionTagList: List<Tag> = listOf(),

    val transactionSectionToShow: TransactionSectionToShow = TransactionSectionToShow.AMOUNT,
) {

    fun addTag(text: String): TransactionEditUiState {

        if (this.transactionTagList.isNotEmpty()) {
            this.transactionTagList.forEach { tag ->
                if (tag.name == text) return this
            }
        }

        val newTag: Tag = Tag(
            name = text,
            count = 1,
        )

        return this.copy(
            transactionTagList = this.transactionTagList + listOf(newTag)
        )
    }
    fun removeTag(position: Int): TransactionEditUiState {

        val mutableList = this.transactionTagList as MutableList
        mutableList.removeAt(position)

        return this.copy(
            transactionTagList = mutableList as List<Tag>
        )
    }
    fun getFullTagList(): List<Tag> {
        if (this.transactionTagList.isEmpty() && this.tagListInDB.isEmpty()) {
            return listOf()
        }
        if (this.tagListInDB.isEmpty()) {
            return this.transactionTagList
        }
        if (this.transactionTagList.isEmpty()) {
            return this.tagListInDB
        }

        val mutableList: MutableList<Tag> = this.tagListInDB as MutableList<Tag>
        var equal = false

        this.transactionTagList.forEach { tag ->
            this.tagListInDB.forEach { tagInDB ->
                if (tag.name == tagInDB.name) {
                    equal = true
                }
            }
            if (!equal) {
                mutableList.add(tag)
            }
        }

        return mutableList
    }

    fun updateCategory(categoryId: UUID): TransactionEditUiState {
        return this.copy(
            transaction = this.transaction.copy(
                idCategory = categoryId
            ),
        )
    }
    fun updateDescription(description: String): TransactionEditUiState {
        return this.copy(
            transaction = this.transaction.copy(
                description = description
            )
        )
    }
    fun updateTransactionDate(date: Date): TransactionEditUiState {
        return this.copy(
            transaction = this.transaction.copy(
                date = date
            )
        )
    }
    fun updateWallet(wallet: Wallet): TransactionEditUiState {
        return this.copy(
            wallet = wallet,
            transaction = this.transaction.copy(
                idWallet = wallet.id
            )
        )
    }
}

class TransactionEditViewModel(
    walletUUID: UUID,
    transactionUUID: UUID,
    transactionType: TransactionType,
) : ViewModel() {

    private var _uiState = MutableStateFlow(TransactionEditUiState())
    val uiState: StateFlow<TransactionEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var calculator = Calculator()

    init {

        val newTransaction = transactionUUID == UUID(0L,0L)

        viewModelScope.launch {
            if (newTransaction) {
                _uiState.value = uiState.value.copy(
                    transaction = uiState.value.transaction.copy(
                        id = UUID.randomUUID(),
                        idWallet = walletUUID,
                        movementType = transactionType.id,
                    ),
                    wallet = repository.getWallet(walletUUID) ?: Wallet(),
                    isLoading = false
                )
            } else {
                val transaction = repository.getTransaction(transactionUUID) ?: Transaction()
                _uiState.value = uiState.value.copy(
                    transaction = transaction,
                    wallet = repository.getWallet(transaction.idWallet) ?: Wallet(),
                    isLoading = false,
                )
            }

            calculator = Calculator.initialize(uiState.value.transaction.amount)

        }

        viewModelScope.launch {
            repository.getCategoryListByTransactionType(transactionType = transactionType).collect { categoryList ->
                _uiState.value = uiState.value.copy(
                    categoryList = categoryList
                )
            }
        }

        viewModelScope.launch {
            repository.getWalletList().collect { walletList ->
                _uiState.value = uiState.value.copy(
                    walletList = walletList
                )
            }
        }

    }

    fun amountKeyPress(key: KeypadDigit) {

        if (key == KeypadDigit.KeyBack) {
            calculator.dropLastDigit()
        } else {
            calculator.addKey(key)
        }

        viewModelScope.launch {
            if (calculator.onScreenString() in listOf("0.","0",".")) {
                _uiState.value = uiState.value.copy(
                    transaction = uiState.value.transaction.copy(
                        amount = 0f
                    ),
                )
            } else {
                _uiState.value = uiState.value.copy(
                    transaction = uiState.value.transaction.copy(
                        amount = calculator.onScreenString().toFloat()
                    )
                )

            }
            _uiState.value = uiState.value.copy(
                amountString = calculator.onScreenString(),
            )
        }
    }

    fun saveTransaction() {
        // TODO: save transaction
    }

    fun setChooseFunctionality(section: TransactionSectionToShow) {
        _uiState.value = uiState.value.copy(
            transactionSectionToShow = section
        )
    }

    fun addTag(tagName: String) {
        _uiState.value = uiState.value.addTag(tagName)
    }
    fun removeTag(tagIndex: Int?) {
        if (tagIndex != null) {
            _uiState.value = uiState.value.removeTag(tagIndex)
        }
    }
    fun updateCategory(category: Category) {
        _uiState.value = uiState.value.updateCategory(category.id)
    }
    fun updateDescription(description: String) {
        _uiState.value = uiState.value.updateDescription(description)
    }
    fun updateTransactionDate(date: Date) {
        _uiState.value = uiState.value.updateTransactionDate(date)
    }

    fun updateWallet(wallet: Wallet) {
        _uiState.value = uiState.value.updateWallet(wallet)
    }

}