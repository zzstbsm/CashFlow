package com.zhengzhou.cashflow.tools

import android.util.Log
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
                    iconName = "grocery",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Eating out",
                    iconName = "eating_out",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Health",
                    iconName = "health",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Transportation",
                    iconName = "transportation",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Travel",
                    iconName = "travel",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Home",
                    iconName = "home",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Subscriptions",
                    iconName = "subscription",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Entertainment",
                    iconName = "entertainment",
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Gift",
                    iconName = "gift",
                    transactionTypeId = TransactionType.Expense.id,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Salary",
                    iconName = "salary",
                    transactionTypeId = TransactionType.Deposit.id
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Deposit",
                    iconName = "wallet",
                    transactionTypeId = TransactionType.Deposit.id
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Movement",
                    iconName = "transfer",
                    transactionTypeId = TransactionType.Move.id
                ),
            )
        }
    }

}