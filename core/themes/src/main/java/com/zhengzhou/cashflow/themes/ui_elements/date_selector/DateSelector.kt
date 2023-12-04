package com.zhengzhou.cashflow.themes.ui_elements.date_selector

import android.text.format.DateFormat
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.zhengzhou.cashflow.R
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    label: String,
    dateFormat: String,
    date: Date,
    onSelectDate: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    selectableDatesCondition: (Long) -> Boolean = { true },
) {
    var showDatePickerState by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return selectableDatesCondition(utcTimeMillis)
            }
        },
    )

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = DateFormat.format(
            dateFormat,
            date
        ).toString(),
        onValueChange = { },
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    showDatePickerState = true
                    focusManager.clearFocus()
                }
            },
        maxLines = 1,
    )

    if (showDatePickerState) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerState = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelectDate(datePickerState.selectedDateMillis)
                        showDatePickerState = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}