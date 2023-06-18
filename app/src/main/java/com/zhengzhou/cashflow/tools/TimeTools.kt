package com.zhengzhou.cashflow.tools

import java.util.Calendar
import java.util.Date

fun getFirstDayOfCurrentMonth() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.DAY_OF_MONTH,1)
    calendar.set(Calendar.HOUR_OF_DAY,0)
    calendar.set(Calendar.MINUTE,0)
    calendar.set(Calendar.SECOND,0)
    calendar.set(Calendar.MILLISECOND,0)
    return calendar.time

}

fun getLastDayOfCurrentMonth() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
    )
    calendar.set(
        Calendar.HOUR_OF_DAY,
        calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
    )
    calendar.set(
        Calendar.MINUTE,
        calendar.getActualMaximum(Calendar.MINUTE),
    )
    calendar.set(
        Calendar.SECOND,
        calendar.getActualMaximum(Calendar.SECOND),
    )
    calendar.set(
        Calendar.MILLISECOND,
        calendar.getActualMaximum(Calendar.MILLISECOND),
    )
    return calendar.time

}