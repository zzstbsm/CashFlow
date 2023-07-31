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
                    iconName = IconsMappedForDB.GROCERY,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Eating out",
                    iconName = IconsMappedForDB.EATING_OUT,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Health",
                    iconName = IconsMappedForDB.HEALTH,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Transportation",
                    iconName = IconsMappedForDB.TRANSPORTATION,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Travel",
                    iconName = IconsMappedForDB.TRAVEL,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Home",
                    iconName = IconsMappedForDB.HOME,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Subscriptions",
                    iconName = IconsMappedForDB.SUBSCRIPTION,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Entertainment",
                    iconName = IconsMappedForDB.ENTERTAINMENT,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Gift",
                    iconName = IconsMappedForDB.GIFT,
                    transactionTypeId = TransactionType.Expense.id,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Salary",
                    iconName = IconsMappedForDB.SALARY,
                    transactionTypeId = TransactionType.Deposit.id
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Deposit",
                    iconName = IconsMappedForDB.WALLET,
                    transactionTypeId = TransactionType.Deposit.id
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Movement",
                    iconName = IconsMappedForDB.TRANSFER,
                    transactionTypeId = TransactionType.Move.id
                ),
            )
        }
    }

}