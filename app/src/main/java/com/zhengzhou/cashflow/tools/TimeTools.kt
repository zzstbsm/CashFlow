package com.zhengzhou.cashflow.tools

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun getToday() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.timeZone = TimeZone.getTimeZone("UTC")
    calendar.add(Calendar.MILLISECOND,TimeZone.getDefault().getOffset(calendar.timeInMillis))

    return calendar.time
}

fun getFirstDayOfCurrentMonth() : Date {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.DAY_OF_MONTH,1)
    calendar.set(Calendar.HOUR_OF_DAY,0)
    calendar.set(Calendar.MINUTE,0)
    calendar.set(Calendar.SECOND,0)
    calendar.set(Calendar.MILLISECOND,0)

    calendar.timeZone = TimeZone.getTimeZone("UTC")
    calendar.add(Calendar.MILLISECOND,TimeZone.getDefault().getOffset(calendar.timeInMillis))

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
        0,
    )
    calendar.set(
        Calendar.MINUTE,
        0,
    )
    calendar.set(
        Calendar.SECOND,
        0,
    )
    calendar.set(
        Calendar.MILLISECOND,
        0,
    )

    calendar.timeZone = TimeZone.getTimeZone("UTC")
    calendar.add(Calendar.MILLISECOND,TimeZone.getDefault().getOffset(calendar.timeInMillis))

    return calendar.time

}