package com.zhengzhou.cashflow.tools

import java.util.Calendar
import java.util.Date

fun getFirstDayOfCurrentMonth() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.DAY_OF_MONTH,1)
    return calendar.time

}

fun getLastDayOfCurrentMonth() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
    )
    return calendar.time

}