package com.zhengzhou.cashflow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.ui.transactionEdit.TransactionSaveResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@Entity(tableName = "movement")
data class Transaction (
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "id_wallet")
    val walletId: UUID,
    @ColumnInfo(name = "id_secondary_wallet")
    val secondaryWalletId: UUID,
    val amount: Float,
    val date: Date,
    @ColumnInfo(name = "id_category")
    val categoryId: UUID,
    val description: String,
    @ColumnInfo(name = "id_location")
    val locationId: UUID?,
    @ColumnInfo(name = "movement_type")
    val transactionType: TransactionType,
    @ColumnInfo(name = "is_blueprint")
    val isBlueprint: Boolean,
) {

    companion object {
        fun newEmpty(): Transaction {
            return Transaction(
                id = UUID(0L,0L),
                walletId = UUID(0L, 0L),
                secondaryWalletId = UUID(0L, 0L),
                amount = 0f,
                date = Date(),
                categoryId = UUID(0L,0L),
                description = "",
                locationId = null,
                transactionType = TransactionType.Loading,
                isBlueprint = false,
            )
        }
    }
}

data class TransactionAndCategory(
    val transaction: Transaction,
    val category: Category,
)

enum class TransactionType (
    val id: Int,
    @StringRes val text: Int,
    @StringRes val newText: Int,
    @DrawableRes val iconId: Int,
    val signInDB: Float,
) {
    Loading(
        id = 0,
        text = R.string.loading,
        newText = R.string.loading,
        iconId = R.drawable.ic_error,
        signInDB = 0f,
    ),
    Move(
        id = 1,
        text = R.string.move,
        newText = R.string.new_move,
        iconId = R.drawable.ic_transfer,
        signInDB = -1f,
    ),
    Deposit(
        id = 2,
        text = R.string.deposit,
        newText = R.string.new_deposit,
        iconId = R.drawable.ic_add,
        signInDB = 1f,
    ),
    Expense(
        id = 3,
        text = R.string.expense,
        newText = R.string.new_expense,
        iconId = R.drawable.ic_remove,
        signInDB = -1f,
    );

    companion object {
        fun setTransaction(
            id: Int
        ): TransactionType? {

            // Return the type of transaction
            TransactionType.values().forEach { transactionType: TransactionType ->
                if (transactionType.id == id) {
                    return transactionType
                }
            }

            // The id is not valid
            return null
        }

        fun getAllActive(): List<TransactionType> {
            return listOf(
                Deposit,
                Expense,
                Move,
            )
        }
    }
}

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
            val wallet = repository.getWallet(transaction.walletId) ?: Wallet.newEmpty()
            val category = repository.getCategory(transaction.categoryId) ?: Category.transfer(transaction.categoryId)

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
                    id = UUID(0L,0L),
                    walletId = wallet.id,
                    secondaryWalletId = secondaryWallet.id,
                    amount = amount,
                    date = date,
                    categoryId = category.id,
                    description = description,
                    locationId = location?.id,
                    transactionType = transactionType,
                    isBlueprint = isBlueprint,
                ),
                wallet = wallet,
                category = category,
                tagList = tagEntryList.map { tagEntry ->
                    Tag.newFromTagEntry(
                        transactionUUID = UUID(0L,0L),
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
                idTransaction = currentTransaction.id,
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