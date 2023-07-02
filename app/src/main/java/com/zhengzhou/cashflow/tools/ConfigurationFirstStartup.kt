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

        suspend fun configureTableCategory() {

            val repository = DatabaseRepository.get()

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

        private fun setDefaultExpenseCategories(): MutableList<Category> {

            return mutableListOf(
                Category(
                    id = UUID(0L,1L),
                    name = R.string.food,
                    idIcon = R.drawable.ic_food,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,2L),
                    name = R.string.health,
                    idIcon = R.drawable.ic_health,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,3L),
                    name = R.string.transportation,
                    idIcon = R.drawable.ic_transportation,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,4L),
                    name = R.string.travel,
                    idIcon = R.drawable.ic_travel,
                    transactionTypeId = TransactionType.Expense.id,
                ),
                Category(
                    id = UUID(0L,5L),
                    name = R.string.home,
                    idIcon = R.drawable.ic_home,
                    transactionTypeId = TransactionType.Expense.id,
                ),
            )
        }

        private fun setDefaultDepositCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID(1L,1L),
                    name = R.string.salary,
                    idIcon = R.drawable.ic_arrow_double_up,
                    transactionTypeId = TransactionType.Deposit.id
                ),
            )
        }

        private fun setDefaultMovementCategories(): MutableList<Category>{
            return mutableListOf(
                Category(
                    id = UUID(2L,1L),
                    name = R.string.move,
                    idIcon = R.drawable.ic_transfer,
                    transactionTypeId = TransactionType.Move.id
                ),
            )
        }
    }

}