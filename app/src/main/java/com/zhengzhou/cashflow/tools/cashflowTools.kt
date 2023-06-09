package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.data.Transaction
import java.util.*

fun addDaysFromDate(date : Date, nDays : Int) : Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH,nDays)
    return calendar.time
}

fun transactionListFilterPeriod(
    transactionList: List<Transaction>,
    startDate: Date,
    endDate: Date,
) : List<Transaction> {
    return transactionList.filter { transaction ->

        fun removeNonDateInfo(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.time
        }

        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()

        calendarStart.time = removeNonDateInfo(startDate)
        calendarEnd.time = removeNonDateInfo(endDate)

        calendarEnd.add(Calendar.DAY_OF_MONTH,1)

        transaction.date >= calendarStart.time && transaction.date < calendarEnd.time
    }
}

fun balanceFlowIn(transactionList: List<Transaction>): Float {

    // TODO: implement the filter for nDays

    var currentAmount = 0f
    transactionList.forEach {transaction ->
        if (transaction.amount > 0f) {
            currentAmount += transaction.amount
        }
    }
    return currentAmount
}
fun balanceFlowOut(transactionList: List<Transaction>): Float {

    // TODO: implement the filter for nDays

    var currentAmount = 0f
    transactionList.forEach { transaction ->
        if (transaction.amount < 0f) {
            currentAmount += transaction.amount
        }
    }
    return currentAmount
}