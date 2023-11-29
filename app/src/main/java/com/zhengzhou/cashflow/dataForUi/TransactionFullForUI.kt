package com.zhengzhou.cashflow.dataForUi

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Location
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.ui.transactionEdit.TransactionSaveResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

data class TransactionFullForUI(
    val transaction: Transaction = Transaction.newEmpty(),
    val wallet: Wallet = Wallet.newEmpty(),
    val category: Category = Category.newEmpty(),
    val tagList: List<Tag> = listOf(),
) {
    companion object {
        suspend fun load(
            repository: DatabaseRepository,
            transactionUUID: UUID
        ): Pair<TransactionFullForUI, Boolean> {

            var isLoading = true
            val jobRetrieveTagTransaction = CoroutineScope(Dispatchers.Default)

            val transaction: Transaction = repository.getTransaction(transactionId = transactionUUID) ?: Transaction.newEmpty()
            val wallet = repository.getWallet(transaction.walletUUID) ?: Wallet.newEmpty()
            val category = repository.getCategory(transaction.categoryUUID) ?: Category.newTransfer(
                transaction.categoryUUID
            )

            var  tagList: List<Tag> = listOf()
            jobRetrieveTagTransaction.launch {
                repository.getTagListFromTransaction(transactionUUID = transaction.id).collect {
                    tagList = it
                    isLoading = false
                }
            }

            while (isLoading) {
                delay(10)
            }
            jobRetrieveTagTransaction.cancel()

            val isLoaded = !isLoading

            return Pair(
                TransactionFullForUI(
                    wallet = wallet,
                    transaction = transaction,
                    category = category,
                    tagList = tagList,
                ),
                isLoaded
            )
        }

        fun new(
            description: String,
            amount: Float,
            date: Date,
            wallet: Wallet,
            secondaryWallet: Wallet,
            category: Category,
            location: Location?,
            tagEntryList: List<TagEntry>,
            transactionType: TransactionType,
            isBlueprint: Boolean,
        ): TransactionFullForUI {
            return TransactionFullForUI(
                transaction = Transaction(
                    id = UUID(0L, 0L),
                    walletUUID = wallet.id,
                    secondaryWalletUUID = secondaryWallet.id,
                    amount = amount,
                    date = date,
                    categoryUUID = category.id,
                    description = description,
                    locationUUID = location?.id,
                    transactionType = transactionType,
                    isBlueprint = isBlueprint,
                ),
                wallet = wallet,
                category = category,
                tagList = tagEntryList.map { tagEntry ->
                    Tag.newFromTagEntry(
                        transactionUUID = UUID(0L, 0L),
                        tagEntry = tagEntry,
                    )
                },
            )
        }
    }

    suspend fun save(
        repository: DatabaseRepository,
        newTransaction: Boolean,
    ): TransactionSaveResult {

        /*
         *
         * Remember to update the count of the tags before saving
         *
         */

        var currentTransaction: Transaction = transaction.copy(
            date = if (newTransaction) Date() else transaction.date,
        )
        var currentTagList: List<Tag> = tagList

        // Save transaction entry
        if (newTransaction) {
            currentTransaction = repository.addTransaction(currentTransaction)
        } else {
            repository.updateTransaction(currentTransaction)
        }

        // Update last access in wallet
        repository.updateWallet(wallet.copy(lastAccess = Date()))

        // Save tags
        currentTagList = currentTagList.map {
            it.copy(
                transactionUUID = currentTransaction.id,
            )
        }
        currentTagList.forEach { tag ->

            val newTag = tag.isNewTag()
            if ((newTag || newTransaction) && tag.enabled) {
                repository.addTag(tag)
            }
            // Tag has been deleted during the edit
            else if (!tag.enabled) {
                repository.deleteTag(tag)
            } else {
                repository.updateTag(tag)
            }
        }

        return TransactionSaveResult.SUCCESS
    }

    suspend fun delete(
        repository: DatabaseRepository,
    ) {
        repository.deleteTransaction(transaction = transaction)
        tagList.map { tag ->
            tag.decreaseCounter()
        }.forEach { tag ->
            repository.deleteTag(tag = tag)
        }
    }
}