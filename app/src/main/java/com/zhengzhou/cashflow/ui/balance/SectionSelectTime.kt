package com.zhengzhou.cashflow.ui.balance

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.TimeTools
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeFilter(
    currentTimeFilter: TimeFilterForSegmentedButton?,
    timeFilterList: List<TimeFilterForSegmentedButton>,
    onSelectTimeFilter: (TimeFilterForSegmentedButton?) -> Unit,
    modifier: Modifier = Modifier,
) {

    MultiChoiceSegmentedButtonRow(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        timeFilterList.forEachIndexed { index, element ->

            val currentSelected = element == currentTimeFilter
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = timeFilterList.size,
                ),
                onCheckedChange = {
                    onSelectTimeFilter(element)
                },
                checked = currentSelected
            ) {
                Text(
                    text = stringResource(id = element.textId)
                )
            }
        }
    }
}

@Composable
fun SelectedPeriodShow(
    startDate: Date,
    endDate: Date,
    onSelectTimePeriod: (Date, Date) -> Unit = { _, _ -> },
    timeFilter: TimeFilterForSegmentedButton?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {

    val arrowButtonEnabled = (timeFilter != TimeFilterForSegmentedButton.All) && (timeFilter != null)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(),
    ) {

        IconButton(
            onClick = {
                when (timeFilter) {
                    TimeFilterForSegmentedButton.Week -> {
                        onSelectTimePeriod(
                            TimeTools.getPreviousWeek(startDate),
                            TimeTools.getPreviousWeek(endDate)
                        )
                    }
                    TimeFilterForSegmentedButton.Month -> {
                        onSelectTimePeriod(
                            TimeTools.getPreviousMonth(startDate),
                            TimeTools.getPreviousMonth(endDate)
                        )
                    }
                    TimeFilterForSegmentedButton.Year -> {
                        onSelectTimePeriod(
                            TimeTools.getPreviousYear(startDate),
                            TimeTools.getPreviousYear(endDate)
                        )
                    }
                    else -> { }
                }
            },
            enabled = arrowButtonEnabled
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_double_left),
                contentDescription = null,
            )
        }

        val personalizedDateFormat = "dd/MM/yyy"
        val startDateFormat =
            DateFormat.format(timeFilter?.dateFormat ?: personalizedDateFormat, startDate)
                .toString()
        val endDateFormat =
            DateFormat.format(timeFilter?.dateFormat ?: personalizedDateFormat, endDate)
                .toString()

        Text(
            text = when (timeFilter) {
                TimeFilterForSegmentedButton.Month -> endDateFormat
                TimeFilterForSegmentedButton.Year -> endDateFormat
                TimeFilterForSegmentedButton.All -> stringResource(id = R.string.Balance_all)
                else -> "$startDateFormat - $endDateFormat"
            }
        )

        IconButton(
            onClick = {
                when (timeFilter) {
                    TimeFilterForSegmentedButton.Week -> {
                        onSelectTimePeriod(
                            TimeTools.getNextWeek(startDate),
                            TimeTools.getNextWeek(endDate)
                        )
                    }
                    TimeFilterForSegmentedButton.Month -> {
                        onSelectTimePeriod(
                            TimeTools.getNextMonth(startDate),
                            TimeTools.getNextMonth(endDate)
                        )
                    }
                    TimeFilterForSegmentedButton.Year -> {
                        onSelectTimePeriod(
                            TimeTools.getNextYear(startDate),
                            TimeTools.getNextYear(endDate)
                        )
                    }
                    else -> { }
                }
            },
            enabled = arrowButtonEnabled,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_double_right),
                contentDescription = null,
            )
        }

    }
}