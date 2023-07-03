package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionFullForUI
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.KeypadDigit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

/*
 * TransactionEditUiState contains all the elements to draw the ui
 *
 * walletList, categoryList and tagListInDB are constant, they just need to be loaded once from the database.
 *
 * To store the tags of the transaction, it uses:
 ***  transactionTagList as List<Tag> -> it contains the list of transactions
 * When the transaction has to be saved, it creates the respective TagTransaction entries for the database
 *
 * Newly added tags in the current edit will have the id UUID(0L,0L) and count 1
 * Tags with UUID != UUID(0L,0L) are tags that have been removed during the current edit.
 * * Proceed to remove it from TagTransaction in the database
 * * Proceed to decrease the counter in the table Tag. If the counter reaches 0, proceed to remove the tag.
 *
 */

enum class TransactionSaveResult {
    SUCCESS,
    NO_CATEGORY,
    NO_AMOUNT,
}

data class TransactionEditUiState(
    val isLoading: Boolean = true,

    val wallet: Wallet = Wallet(),
    val transaction: Transaction = Transaction(),
    val currentTagList: List<Tag> = listOf(),

    val walletList: List<Wallet> = listOf(),
    val categoryList: List<Category> = listOf(),
    val tagListInDB: List<TagEntry> = listOf(),

    val amountString: String = "0",
    val transactionSectionToShow: TransactionSectionToShow = TransactionSectionToShow.AMOUNT,
) {

    fun addTag(text: String): TransactionEditUiState {

        // Clean text
        var cleanText = text
        while (cleanText.isNotEmpty() && cleanText.last() == ' ') {
            cleanText = cleanText.dropLast(1)
        }
        if (cleanText.isEmpty()) return this

        // Ignore the add tag if the tag is already added
        if (this.currentTagList.isNotEmpty()) {
            this.currentTagList.forEach { tag ->
                if (tag.name == cleanText) return this
            }
        }

        // Check if it exists in tagListInDB
        val filteredDB = this.tagListInDB.filter {
            it.name == cleanText
        }
        val tagAlreadyExist = filteredDB.isNotEmpty()
        val tagEntry = if (tagAlreadyExist) {
            filteredDB.first().copy(
                count = filteredDB.first().count + 1
            )
        } else {
            TagEntry(
                id = UUID.randomUUID(),
                name = cleanText,
                count = 1
            )
        }
        val tag = Tag.newFromTagEntry(
            transactionUUID = transaction.id,
            tagEntry = tagEntry
        )

        val newCurrentTagList = this.currentTagList + listOf(tag)

        return this.copy(
            currentTagList = newCurrentTagList
        )
    }
    fun disableTag(position: Int): TransactionEditUiState {

        val tempTagList = this.currentTagList as MutableList

        tempTagList[position] = tempTagList[position].copy(
            count = tempTagList[position].count - 1,
            enabled = false
        )

        return this.copy(
            currentTagList = tempTagList
        )

    }
    fun enableTag(position: Int) : TransactionEditUiState {
        val tempTagList = this.currentTagList as MutableList

        tempTagList[position] = tempTagList[position].copy(
            count = tempTagList[position].count + 1,
            enabled = true
        )

        return this.copy(
            currentTagList = tempTagList
        )
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
    transactionUUID: UUID,
    transactionType: TransactionType,
) : ViewModel() {

    private var _uiState = MutableStateFlow(TransactionEditUiState())
    val uiState: StateFlow<TransactionEditUiState> = _uiState.asStateFlow()

    private var newTransaction: Boolean = false

    private val repository = DatabaseRepository.get()

    private var calculator = Calculator()

    init {

        newTransaction = transactionUUID == UUID(0L,0L)

        var isLoadedTransactionFull: Boolean

        var wallet: Wallet
        var transaction: Transaction
        var currentTagList: List<Tag> = listOf()

        val jobCollectWalletList = CoroutineScope(Dispatchers.Default)
        var walletListCollected = false
        var walletList: List<Wallet> = listOf()
        val jobCollectCategoryList = CoroutineScope(Dispatchers.Default)
        var categoryListCollected = false
        var categoryList: List<Category> = listOf()
        val jobCollectTagListInDB = CoroutineScope(Dispatchers.Default)
        var tagListInDBCollected = false
        var tagListInDB: List<TagEntry> = listOf()

        jobCollectWalletList.launch {
            repository.getWalletList().collect {
                walletList = it
                walletListCollected = true
            }
        }
        jobCollectCategoryList.launch {
            repository.getCategoryList().collect {
                categoryList = it.filter { category ->
                    category.transactionTypeId == transactionType.id
                }
                categoryListCollected = true
            }
        }
        jobCollectTagListInDB.launch {
            repository.getTagEntryList().collect {
                tagListInDB = it
                tagListInDBCollected = true
            }
        }

        viewModelScope.launch {

            if (newTransaction) {

                wallet = repository.getWalletLastAccessed() ?: Wallet()
                transaction = Transaction(
                    idWallet = wallet.id,
                    movementType = transactionType.id
                )
                isLoadedTransactionFull = true

            } else {
                val (transactionFullForUI, isLoaded) = TransactionFullForUI.load(repository, transactionUUID)
                wallet = transactionFullForUI.wallet
                transaction = transactionFullForUI.transaction
                currentTagList = transactionFullForUI.tagList

                isLoadedTransactionFull = isLoaded
            }


            while (!(walletListCollected && tagListInDBCollected && categoryListCollected && isLoadedTransactionFull)) {
                delay(10)
            }

            calculator = Calculator.initialize(
                when (transactionType) {
                    TransactionType.Deposit -> transaction.amount
                    TransactionType.Expense -> -transaction.amount
                    else -> 0f // TODO: To handle movement transaction
                }
            )
            _uiState.value = TransactionEditUiState(
                isLoading = false,

                wallet = wallet,
                transaction = transaction,
                currentTagList = currentTagList,

                walletList = walletList,
                categoryList = categoryList,
                tagListInDB = tagListInDB,

                amountString = calculator.onScreenString()
            )
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

                val sign = when (uiState.value.transaction.movementType) {
                    TransactionType.Deposit.id -> 1f
                    TransactionType.Expense.id -> -1f
                    else -> 0f // TODO: to fix
                }

                val toSaveAmount = calculator.onScreenString().toFloat() * sign
                _uiState.value = uiState.value.copy(
                    transaction = uiState.value.transaction.copy(
                        amount = toSaveAmount
                    )
                )

            }
            _uiState.value = uiState.value.copy(
                amountString = calculator.onScreenString(),
            )
        }
    }

    fun saveTransaction(): TransactionSaveResult {


        var transaction: Transaction = uiState.value.transaction
        var currentTransactionTagList: List<Tag> = uiState.value.currentTagList

        val ifCategoryChosen = transaction.idCategory != UUID(0L,0L)
        val ifAmountChosen = transaction.amount >= 0.01f || transaction.amount <= -0.01f

        if (!ifAmountChosen) {
            return TransactionSaveResult.NO_AMOUNT
        }
        if (!ifCategoryChosen) {
            return TransactionSaveResult.NO_CATEGORY
        }

        // Save transaction
        viewModelScope.launch {

            // Save transaction entry
            if (newTransaction) {
                transaction = repository.addTransaction(transaction)
            } else {
                repository.updateTransaction(transaction)
            }

            // Save tags
            currentTransactionTagList = currentTransactionTagList.map {
                it.copy(
                    idTransaction = transaction.id
                )
            }
            currentTransactionTagList.forEach { tag ->

                val newTag = tag.id == UUID(0L, 0L)

                if (newTag && tag.enabled) {
                    repository.addTag(tag)
                }
                // Tag has been deleted during the edit
                else if (!tag.enabled) {
                    repository.deleteTag(tag)
                } else {
                    repository.updateTag(tag)
                }
            }
            // TODO: Save location
        }
        return TransactionSaveResult.SUCCESS
    }

    fun setChooseFunctionality(section: TransactionSectionToShow) {
        _uiState.value = uiState.value.copy(
            transactionSectionToShow = section
        )
    }

    fun addTag(tagName: String) {
        _uiState.value = uiState.value.addTag(tagName)
    }
    fun disableTag(tagIndex: Int?) {
        if (tagIndex != null) {
            _uiState.value = uiState.value.disableTag(tagIndex)
        }
    }
    fun enableTag(tagIndex: Int?) {
        if (tagIndex != null) {
            _uiState.value = uiState.value.enableTag(tagIndex)
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