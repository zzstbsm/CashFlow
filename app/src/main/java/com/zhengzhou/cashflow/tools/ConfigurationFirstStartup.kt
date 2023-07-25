package com.zhengzhou.cashflow.tools

import android.util.Log
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.database.DatabaseRepository
import java.util.UUID

const val TAG = "ConfigurationFirstStartup"

class ConfigurationFirstStartup {

    companion object {

        suspend fun configureTableCategory(
            repository: DatabaseRepository = DatabaseRepository.get()
        ) {

            setDefaultExpenseCategories().forEach {category: Category ->
                Log.d(TAG,"Adding ${category.name}")
                repository.addCategory(category)
            }
            setDefaultDepositCategories().forEach { category: Category ->
                Log.d(TAG, "Adding ${category.name}")
                repository.addCategory(category)
            }
            setDefaultMovementCategories().forEach {category: Category ->
                Log.d(TAG,"Adding ${category.name}")
                repository.addCategory(category)
            }

        }

        fun setDefaultExpenseCategories(): MutableList<Category> {

            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Groceries",
                    idIcon = R.drawable.ic_grocery,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Eating out",
                    idIcon = R.drawable.ic_eating_out,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Health",
                    idIcon = R.drawable.ic_health,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Transportation",
                    idIcon = R.drawable.ic_transportation,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Travel",
                    idIcon = R.drawable.ic_travel,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Home",
                    idIcon = R.drawable.ic_home,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Subscriptions",
                    idIcon = R.drawable.ic_subscriptions,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Entertainment",
                    idIcon = R.drawable.ic_subscriptions,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Gift",
                    idIcon = R.drawable.ic_gift,
                    transactionTypeId = TransactionType.Expense.id,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Salary",
                    idIcon = R.drawable.ic_arrow_double_up,
                    transactionTypeId = TransactionType.Deposit.id
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Deposit",
                    idIcon = R.drawable.ic_wallet,
                    transactionTypeId = TransactionType.Deposit.id
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Movement",
                    idIcon = R.drawable.ic_transfer,
                    transactionTypeId = TransactionType.Move.id
                ),
            )
        }
    }

}