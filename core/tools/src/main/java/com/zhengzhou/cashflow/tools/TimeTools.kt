package com.zhengzhou.cashflow.tools

import java.util.Calendar
import java.util.Date

class TimeTools {
    companion object {

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

            return calendar.time
        }

        fun timeSetBeginningOfDay(date: Date): Date {

            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(
                Calendar.HOUR_OF_DAY,0
            )
            calendar.set(
                Calendar.MINUTE,0
            )
            calendar.set(
                Calendar.SECOND,0
            )
            calendar.set(
                Calendar.MILLISECOND,0
            )
            return calendar.time
        }

        fun timeSetEndOfDay(date: Date): Date {

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY,23)
            calendar.set(Calendar.MINUTE,59)
            calendar.set(Calendar.SECOND,59)
            calendar.set(Calendar.MILLISECOND,999)

            return calendar.time
        }

        fun getPreviousWeek(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.DAY_OF_MONTH,-7)
            return calendar.time
        }
        fun getNextWeek(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.DAY_OF_MONTH,7)
            return calendar.time
        }
        fun getPreviousMonth(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.MONTH,-1)
            return calendar.time
        }
        fun getNextMonth(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.MONTH,1)
            return calendar.time
        }
        fun getPreviousYear(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.YEAR,-1)
            return calendar.time
        }
        fun getNextYear(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.add(Calendar.YEAR,1)
            return calendar.time
        }
    }
}