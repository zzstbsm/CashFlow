package com.zhengzhou.cashflow

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
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
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.GROCERY,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Eating out",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.EATING_OUT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Health",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.HEALTH,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Transportation",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.TRANSPORTATION,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Travel",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.TRAVEL,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Home",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.HOME,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Subscriptions",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.SUBSCRIPTION,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Entertainment",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.ENTERTAINMENT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Gift",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.GIFT,
                    transactionType = TransactionType.Expense,
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Sport",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.SPORT,
                    transactionType = TransactionType.Expense,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Salary",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.SALARY,
                    transactionType = TransactionType.Deposit
                ),
                Category(
                    id = UUID.randomUUID(),
                    name = "Deposit",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.WALLET,
                    transactionType = TransactionType.Deposit
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID.randomUUID(),
                    name = "Transfer",
                    iconName = com.zhengzhou.cashflow.themes.IconsMappedForDB.TRANSFER,
                    transactionType = TransactionType.Move
                ),
            )
        }
    }

}