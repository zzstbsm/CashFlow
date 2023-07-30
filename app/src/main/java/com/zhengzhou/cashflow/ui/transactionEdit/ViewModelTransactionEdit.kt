package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.TagLocation
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionFullForUI
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.KeypadDigit
import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
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
)

class TransactionEditViewModel(
    transactionUUID: UUID,
    transactionType: TransactionType,
    isBlueprint: Boolean,
    editBlueprint: Boolean,
) : ViewModel() {

    private var _uiState = MutableStateFlow(TransactionEditUiState())
    val uiState: StateFlow<TransactionEditUiState> = _uiState.asStateFlow()

    private var newTransaction: Boolean = false

    private val repository = DatabaseRepository.get()

    private var writingOnUiState: Boolean = false

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
                    movementType = transactionType.id,
                    isBlueprint = (isBlueprint && editBlueprint),
                )
                isLoadedTransactionFull = true

            } else {
                val (transactionFullForUI, isLoaded) = TransactionFullForUI.load(repository, transactionUUID)
                wallet = transactionFullForUI.wallet
                transaction = transactionFullForUI.transaction.copy(
                    isBlueprint = (isBlueprint && editBlueprint)
                )
                currentTagList = transactionFullForUI.tagList

                isLoadedTransactionFull = isLoaded
            }
            newTransaction = newTransaction || (isBlueprint && !editBlueprint)


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
                categoryList = categoryList.sortedBy { it.name },
                tagListInDB = tagListInDB,

                amountString = calculator.onScreenString()
            )
        }

    }

    private fun setUiState(
        isLoading: Boolean? = null,

        wallet: Wallet? = null,
        transaction: Transaction? = null,
        currentTagList: List<Tag>? = null,

        walletList: List<Wallet>? = null,
        categoryList: List<Category>? = null,
        tagListInDB: List<TagEntry>? = null,

        amountString: String? = null,
        transactionSectionToShow: TransactionSectionToShow? = null,
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)

            writingOnUiState = true

            _uiState.value = TransactionEditUiState(
                isLoading = isLoading ?: uiState.value.isLoading,

                wallet = wallet ?: uiState.value.wallet,
                transaction = transaction ?: uiState.value.transaction,
                currentTagList = currentTagList ?: uiState.value.currentTagList,

                walletList = walletList ?: uiState.value.walletList,
                categoryList = categoryList ?: uiState.value.categoryList,
                tagListInDB = tagListInDB ?: uiState.value.tagListInDB,

                amountString = amountString ?: uiState.value.amountString,
                transactionSectionToShow = transactionSectionToShow ?: uiState.value.transactionSectionToShow,
            )

            writingOnUiState = false

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
                setUiState(
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
                setUiState(
                    transaction = uiState.value.transaction.copy(
                        amount = toSaveAmount
                    )
                )

            }
            setUiState(
                amountString = calculator.onScreenString(),
            )
        }
    }

    fun saveTransaction(): TransactionSaveResult {

        val transaction: Transaction = uiState.value.transaction
        val currentTransactionTagList: List<Tag> = uiState.value.currentTagList

        val ifCategoryChosen = transaction.idCategory != UUID(0L,0L)
        val ifAmountChosen = transaction.amount >= 0.01f || transaction.amount <= -0.01f

        if (!ifAmountChosen) {
            return TransactionSaveResult.NO_AMOUNT
        }
        if (!ifCategoryChosen) {
            return TransactionSaveResult.NO_CATEGORY
        }

        val transactionFullForUI = TransactionFullForUI(
            transaction = uiState.value.transaction,
            wallet = uiState.value.wallet,
            category = uiState.value.categoryList.first {
                it.id == uiState.value.transaction.idCategory
            },
            tagList = currentTransactionTagList,
            location = TagLocation(),
        )
        viewModelScope.launch {
            transactionFullForUI.save(
                repository = repository,
                newTransaction = newTransaction,
            )
        }
        return TransactionSaveResult.SUCCESS
    }

    fun setChooseFunctionality(section: TransactionSectionToShow) {
        setUiState(
            transactionSectionToShow = section
        )
    }

    fun addTag(tagName: String) {
        // Clean text
        val cleanText = removeSpaceFromStringEnd(tagName)
        if (cleanText.isEmpty()) return

        // Ignore the add tag if the tag is already added
        if (uiState.value.currentTagList.isNotEmpty()) {
            uiState.value.currentTagList.forEach { tag ->
                if (tag.name == cleanText) return
            }
        }

        // Check if it exists in tagListInDB
        val filteredDB = uiState.value.tagListInDB.filter {
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
            transactionUUID = uiState.value.transaction.id,
            tagEntry = tagEntry
        )

        val newCurrentTagList = uiState.value.currentTagList + listOf(tag)

        setUiState(
            currentTagList = newCurrentTagList
        )
    }
    fun disableTag(tagIndex: Int?) {
        if (tagIndex != null) {

            val tempTagList = uiState.value.currentTagList as MutableList

            tempTagList[tagIndex] = tempTagList[tagIndex].copy(
                count = tempTagList[tagIndex].count - 1,
                enabled = false
            )

            setUiState(
                currentTagList = tempTagList
            )
        }
    }
    fun enableTag(tagIndex: Int?) {
        if (tagIndex != null) {
            val tempTagList = uiState.value.currentTagList as MutableList

            tempTagList[tagIndex] = tempTagList[tagIndex].copy(
                count = tempTagList[tagIndex].count + 1,
                enabled = true
            )

            setUiState(
                currentTagList = tempTagList
            )
        }
    }
    fun updateCategory(category: Category) {

        setUiState(
            transaction = uiState.value.transaction.copy(
                idCategory = category.id
            ),
        )
    }
    fun updateDescription(description: String) {
        setUiState(
            transaction = uiState.value.transaction.copy(
                description = description
            )
        )
    }
    fun updateTransactionDate(date: Date) {
        setUiState(
            transaction = uiState.value.transaction.copy(
                date = date
            )
        )
    }

    fun updateWallet(wallet: Wallet) {
        setUiState(
            transaction = uiState.value.transaction.copy(
                idWallet = wallet.id
            )
        )
    }

}