package com.zhengzhou.cashflow.transaction_edit.view_model

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Location
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.TagUseCases
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.TransactionUseCases
import com.zhengzhou.cashflow.transaction_edit.return_results.TransactionSaveResult
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
            repository: RepositoryInterface,
            transactionUUID: UUID
        ): Pair<TransactionFullForUI, Boolean> {

            val tagUseCases = TagUseCases(repository)

            var isLoading = true
            val jobRetrieveTagTransaction = CoroutineScope(Dispatchers.Default)

            val transaction: Transaction = repository.getTransaction(transactionId = transactionUUID) ?: Transaction.newEmpty()
            val wallet = repository.getWallet(transaction.walletUUID) ?: Wallet.newEmpty()
            val category = repository.getCategory(transaction.categoryUUID) ?: Category.newTransfer(
                transaction.categoryUUID
            )

            var  tagList: List<Tag> = listOf()
            jobRetrieveTagTransaction.launch {
                tagUseCases.getTagListFromTransaction(transactionUUID = transaction.id).collect {
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
        repository: RepositoryInterface,
        newTransaction: Boolean,
    ): TransactionSaveResult {

        /**
         *
         * Remember to update the count of the tags before saving
         *
         */

        val tagUseCases = TagUseCases(repository)
        val transactionUseCases = TransactionUseCases(repository)

        var currentTransaction: Transaction = transaction.copy(
            date = if (newTransaction) Date() else transaction.date,
        )
        var currentTagList: List<Tag> = tagList

        // Save transaction entry
        if (newTransaction) {
            currentTransaction = transactionUseCases.addTransaction(currentTransaction)
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
                tagUseCases.addTag(tag)
            }
            // Tag has been deleted during the edit
            else if (!tag.enabled) {
                tagUseCases.deleteTag(tag)
            } else {
                tagUseCases.updateTag(tag)
            }
        }

        return TransactionSaveResult.SUCCESS
    }

    suspend fun delete(
        repository: RepositoryInterface,
    ) {
        val tagUseCases = TagUseCases(repository)
        val transactionUseCases = TransactionUseCases(repository)
        transactionUseCases.deleteTransaction(transaction = transaction)
        tagList.map { tag ->
            tag.decreaseCounter()
        }.forEach { tag ->
            tagUseCases.deleteTag(tag = tag)
        }
    }
}