package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.dataForUi.TransactionFullForUI
import com.zhengzhou.cashflow.tools.calculator.Calculator
import com.zhengzhou.cashflow.tools.calculator.KeypadDigit
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

data class TransactionEditUiState(
    val isLoading: Boolean = true,

    val wallet: Wallet = Wallet.newEmpty(),
    val transaction: Transaction = Transaction.newEmpty(),
    val currentTagList: List<Tag> = listOf(),
    val secondaryWallet: Wallet? = null,

    val walletList: List<Wallet> = listOf(),
    val categoryList: List<Category> = listOf(),
    val tagListInDB: List<TagEntry> = listOf(),

    val amountString: String = "0",
    val transactionSectionToShow: TransactionSectionToShow,
)

class TransactionEditViewModel(
    transactionUUID: UUID,
    transactionType: TransactionType,
    currency: Currency,
    isBlueprint: Boolean,
    editBlueprint: Boolean,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        TransactionEditUiState(
            transactionSectionToShow = TransactionEditScreenToChooseFunctionality.first {
                transactionType in it.transactionTypeList
            },
        )
    )
    val uiState: StateFlow<TransactionEditUiState> = _uiState.asStateFlow()

    private var newTransaction: Boolean = false

    private val repository = DatabaseRepository.get()

    private var writingOnUiState: Boolean = false

    private var calculator = Calculator()

    init {

        newTransaction = transactionUUID == UUID(0L, 0L)

        var isLoadedTransactionFull: Boolean

        var wallet: Wallet
        var transaction: Transaction
        var currentTagList: List<Tag> = listOf()

        val collectInfoJob = CoroutineScope(Dispatchers.Default)

        var walletListCollected = false
        var walletList: List<Wallet> = listOf()
        var categoryListCollected = false
        var categoryList: List<Category> = listOf()
        var tagListInDBCollected = false
        var tagListInDB: List<TagEntry> = listOf()

        collectInfoJob.launch {
            launch {
                repository.getWalletListByCurrency(
                    currency = currency
                ).collect {
                    walletList = it
                    walletListCollected = true
                }
            }
            launch {
                repository.getCategoryList().collect {
                    categoryList = it.filter { category ->
                        category.transactionType == transactionType
                    }
                    categoryListCollected = true
                }
            }
            launch {
                repository.getTagEntryList().collect {
                    tagListInDB = it
                    tagListInDBCollected = true
                }
            }
        }

        viewModelScope.launch {

            if (newTransaction) {

                wallet = repository.getWalletLastAccessed() ?: Wallet.newEmpty()
                transaction = Transaction.newEmpty().copy(
                    walletUUID = wallet.id,
                    transactionType = transactionType,
                    isBlueprint = (isBlueprint && editBlueprint),
                )
                isLoadedTransactionFull = true

            } else {
                val (transactionFullForUI, isLoaded) = TransactionFullForUI.load(
                    repository,
                    transactionUUID
                )
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

            // Data collection completed

            // Set amount
            calculator = Calculator.initialize(
                when (transactionType) {
                    TransactionType.Deposit -> transaction.amount
                    TransactionType.Expense -> -transaction.amount
                    TransactionType.Move -> -transaction.amount
                    else -> 0f // Case that should not happen in any case except loading
                }
            )

            // Set secondary wallet
            val secondaryWallet = if (walletList.size > 1) {
                walletList.sortedByDescending { it.lastAccess }[1]
            } else walletList[0]

            _uiState.value = uiState.value.copy(
                isLoading = false,

                wallet = wallet,
                transaction = if (transactionType == TransactionType.Move) {
                    transaction.copy(
                        secondaryWalletUUID = secondaryWallet.id,
                        categoryUUID = categoryList.first().id // There is only one category if it is a newTransfer
                    )
                } else transaction,

                currentTagList = currentTagList,
                secondaryWallet = secondaryWallet,

                walletList = walletList,
                categoryList = categoryList.sortedBy { it.name },
                tagListInDB = tagListInDB,

                amountString = calculator.onScreenString(),
            )
        }

    }

    private fun setUiState(
        isLoading: Boolean? = null,

        wallet: Wallet? = null,
        transaction: Transaction? = null,
        currentTagList: List<Tag>? = null,
        secondaryWallet: Wallet? = null,

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
                secondaryWallet = secondaryWallet ?: uiState.value.secondaryWallet,

                walletList = walletList ?: uiState.value.walletList,
                categoryList = categoryList ?: uiState.value.categoryList,
                tagListInDB = tagListInDB ?: uiState.value.tagListInDB,

                amountString = amountString ?: uiState.value.amountString,
                transactionSectionToShow = transactionSectionToShow
                    ?: uiState.value.transactionSectionToShow,
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
            if (calculator.onScreenString() in listOf("0.", "0", ".")) {
                setUiState(
                    transaction = uiState.value.transaction.copy(
                        amount = 0f
                    ),
                )
            } else {

                val sign = uiState.value.transaction.transactionType.signInDB

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

        val wallet: Wallet = uiState.value.wallet
        val category: Category? = uiState.value.categoryList.firstOrNull {
            it.id == transaction.categoryUUID
        }
        val currentTransactionTagList: List<Tag> = uiState.value.currentTagList
        val transactionType: TransactionType = uiState.value.transaction.transactionType

        val ifCategoryChosen = transaction.categoryUUID != UUID(0L, 0L)
        val ifAmountChosen = transaction.amount >= 0.01f || transaction.amount <= -0.01f

        if (
            TransactionSaveResult.NO_AMOUNT.checkIfThrowError(
                checkIfThrow = !ifAmountChosen,
                transactionType = transactionType
            )
        ) return TransactionSaveResult.NO_AMOUNT
        if (
            TransactionSaveResult.NO_CATEGORY.checkIfThrowError(
                checkIfThrow = !ifCategoryChosen,
                transactionType = transactionType
            )
        ) return TransactionSaveResult.NO_CATEGORY

        val transactionFullForUI = TransactionFullForUI(
            transaction = transaction,
            wallet = wallet,
            category = category!!,
            tagList = currentTransactionTagList,
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

            tempTagList[tagIndex] = tempTagList[tagIndex].decreaseCounter().disableTag()

            setUiState(
                currentTagList = tempTagList
            )
        }
    }

    fun enableTag(tagIndex: Int?) {
        if (tagIndex != null) {
            val tempTagList = uiState.value.currentTagList as MutableList

            tempTagList[tagIndex] = tempTagList[tagIndex].increaseCounter().enableTag()

            setUiState(
                currentTagList = tempTagList
            )
        }
    }

    fun updateCategory(category: Category) {

        setUiState(
            transaction = uiState.value.transaction.copy(
                categoryUUID = category.id
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
            wallet = wallet,
            transaction = uiState.value.transaction.copy(
                walletUUID = wallet.id
            )
        )
    }

    fun updateSecondaryWallet(wallet: Wallet) {
        setUiState(
            secondaryWallet = wallet,
            transaction = uiState.value.transaction.copy(
                categoryUUID = wallet.id
            )
        )
    }
}