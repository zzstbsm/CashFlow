package com.zhengzhou.cashflow.total_balance

import com.zhengzhou.cashflow.tools.TimeTools
import java.util.Calendar
import java.util.Date

enum class TimeFilterForSegmentedButton(
    val textId: Int,
    val dateFormat: String,
) {
    Week(
        textId = R.string.week,
        dateFormat = "EE, dd MMM",
    ),
    Month(
        textId = R.string.month,
        dateFormat = "MMMM yyyy",
    ),
    Year(
        textId = R.string.year,
        dateFormat = "yyyy",
    ),
    All(
        textId = R.string.all,
        dateFormat = "dd/MM/yyyy"
    );

    /**
     * @return The first date of the selected period: Monday, first day of month, first day of year, epoch
     */
    fun getStartDate(): Date {

        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = TimeTools.timeSetBeginningOfDay(Date())

        when(this) {

            Week -> startDateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            Month -> startDateCalendar.set(Calendar.DAY_OF_MONTH, 1)
            Year -> startDateCalendar.set(Calendar.DAY_OF_YEAR, 1)
            All -> {
                val epochTime = Date()
                epochTime.time = 0L
                startDateCalendar.time = epochTime
            }
        }

        return startDateCalendar.time
    }

    /**
     * @return The last date of the selected period: Sunday, last day of month, last day of year, current time
     */
    fun getEndDate(): Date {

        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = Date()

        endDateCalendar.set(
            Calendar.HOUR_OF_DAY, 23
        )
        endDateCalendar.set(
            Calendar.MINUTE, 59
        )
        endDateCalendar.set(
            Calendar.SECOND, 59
        )
        endDateCalendar.set(
            Calendar.MILLISECOND, 999
        )

        when(this) {

            Week -> endDateCalendar.set(
                Calendar.DAY_OF_WEEK,
                Calendar.SUNDAY
            )
            Month -> endDateCalendar.set(
                Calendar.DAY_OF_MONTH,
                endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            Year -> endDateCalendar.set(
                Calendar.DAY_OF_YEAR,
                endDateCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)
            )
            All -> { }
        }

        return endDateCalendar.time
    }
}