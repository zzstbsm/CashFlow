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
import java.util.*

@Entity(tableName = "movement")
data class Transaction (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_wallet")
    val idWallet: UUID = UUID(0L,0L),
    val amount: Float = 0f,
    val date: Date = Date(),
    @ColumnInfo(name = "id_category")
    val idCategory: UUID = UUID(0L,0L),
    val description: String = "",
    @ColumnInfo(name = "id_location")
    val idLocation: UUID = UUID(0L,0L),
    @ColumnInfo(name = "movement_type")
    val movementType: Int = TransactionType.Loading.id,
    @ColumnInfo(name = "is_blueprint")
    val isBlueprint: Boolean = false,
) {
    suspend fun getCategory() : Category? {
        val repository = DatabaseRepository.get()
        return repository.getCategory(idCategory)
    }

    fun isNewTransaction(): Boolean {
        return this.id == UUID(0L,0L)
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
) {
    Loading(
        id = 0,
        text = R.string.loading,
        newText = R.string.loading,
        iconId = R.drawable.ic_error,
    ),
    Move(
        id = 1,
        text = R.string.move,
        newText = R.string.new_move,
        iconId = R.drawable.ic_transfer
    ),
    Deposit(
        id = 2,
        text = R.string.deposit,
        newText = R.string.new_deposit,
        iconId = R.drawable.ic_add
    ),
    Expense(
        id = 3,
        text = R.string.expense,
        newText = R.string.new_expense,
        iconId = R.drawable.ic_remove
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
    }
}

data class TransactionFullForUI(
    val transaction: Transaction = Transaction(),
    val wallet: Wallet = Wallet(),
    val category: Category = Category(),
    val tagList: List<Tag> = listOf(),
    val location: TagLocation = TagLocation(),
) {
    companion object {
        suspend fun load(
            repository: DatabaseRepository,
            transactionUUID: UUID
        ): Pair<TransactionFullForUI, Boolean> {

            var isLoading = true
            val jobRetrieveTagTransaction = CoroutineScope(Dispatchers.Default)

            val transaction: Transaction = repository.getTransaction(transactionId = transactionUUID) ?: Transaction()
            val wallet = repository.getWallet(transaction.idWallet) ?: Wallet()
            val category = repository.getCategory(transaction.idCategory) ?: Category()

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

            val location = repository.getLocation(transaction.idLocation) ?: TagLocation()

            val isLoaded = !isLoading

            return Pair(
                TransactionFullForUI(
                    wallet = wallet,
                    transaction = transaction,
                    category = category,
                    tagList = tagList,
                    location = location,
                ),
                isLoaded
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
        // TODO: Save location

        return TransactionSaveResult.SUCCESS
    }

    suspend fun delete(
        repository: DatabaseRepository,
    ) {
        repository.deleteTransaction(transaction = transaction)
        tagList.map { tag ->
            tag.copy(
                count = tag.count - 1
            )
        }.forEach { tag ->
            repository.deleteTag(tag = tag)
        }

        // TODO: delete location
    }
}