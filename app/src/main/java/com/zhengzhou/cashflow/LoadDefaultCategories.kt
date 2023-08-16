package com.zhengzhou.cashflow

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import java.util.UUID

class LoadDefaultCategories {

    companion object {

        suspend fun configureTableCategory(
            repository: com.zhengzhou.cashflow.database.DatabaseRepository = com.zhengzhou.cashflow.database.DatabaseRepository.get()
        ) {

            setDefaultExpenseCategories().forEach { category: Category ->
                repository.addCategory(category)
            }
            setDefaultDepositCategories().forEach { category: Category ->
                repository.addCategory(category)
            }
            setDefaultMovementCategories().forEach { category: Category ->
                repository.addCategory(category)
            }

        }

        fun setDefaultExpenseCategories(): MutableList<Category> {

            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Groceries",
                    iconName = IconsMappedForDB.GROCERY,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Eating out",
                    iconName = IconsMappedForDB.EATING_OUT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Health",
                    iconName = IconsMappedForDB.HEALTH,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Transportation",
                    iconName = IconsMappedForDB.TRANSPORTATION,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Travel",
                    iconName = IconsMappedForDB.TRAVEL,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Home",
                    iconName = IconsMappedForDB.HOME,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Subscriptions",
                    iconName = IconsMappedForDB.SUBSCRIPTION,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Entertainment",
                    iconName = IconsMappedForDB.ENTERTAINMENT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Gift",
                    iconName = IconsMappedForDB.GIFT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Sport",
                    iconName = IconsMappedForDB.SPORT,
                    transactionType = TransactionType.Expense,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Salary",
                    iconName = IconsMappedForDB.SALARY,
                    transactionType = TransactionType.Deposit
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Deposit",
                    iconName = IconsMappedForDB.WALLET,
                    transactionType = TransactionType.Deposit
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Transfer",
                    iconName = IconsMappedForDB.TRANSFER,
                    transactionType = TransactionType.Move
                ),
            )
        }
    }

}