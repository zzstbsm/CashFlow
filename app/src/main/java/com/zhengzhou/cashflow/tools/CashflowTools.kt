package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.data.Transaction

fun balanceFlowIn(transactionList: List<Transaction>): Float {

    var currentAmount = 0f
    transactionList.forEach {transaction ->
        if (transaction.amount > 0f) {
            currentAmount += transaction.amount
        }
    }
    return currentAmount
}
fun balanceFlowOut(transactionList: List<Transaction>): Float {

    var currentAmount = 0f
    transactionList.forEach { transaction ->
        if (transaction.amount < 0f) {
            currentAmount += transaction.amount
        }
    }
    return currentAmount
}