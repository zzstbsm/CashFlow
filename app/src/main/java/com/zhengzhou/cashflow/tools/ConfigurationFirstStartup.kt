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
                    id = UUID(0L,1L),
                    name = "Food",
                    idIcon = R.drawable.ic_food,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,2L),
                    name = "Health",
                    idIcon = R.drawable.ic_health,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,3L),
                    name = "Transportation",
                    idIcon = R.drawable.ic_transportation,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,4L),
                    name = "Travel",
                    idIcon = R.drawable.ic_travel,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,5L),
                    name = "Home",
                    idIcon = R.drawable.ic_home,
                    transactionTypeId = TransactionType.Expense.id,
                ),
            )
        }

        fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID(1L,1L),
                    name = "Salary",
                    idIcon = R.drawable.ic_arrow_double_up,
                    transactionTypeId = TransactionType.Deposit.id
                ),
            )
        }

        fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID(2L,1L),
                    name = "Movement",
                    idIcon = R.drawable.ic_transfer,
                    transactionTypeId = TransactionType.Move.id
                ),
            )
        }
    }

}