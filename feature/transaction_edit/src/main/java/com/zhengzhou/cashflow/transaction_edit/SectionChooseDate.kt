package com.zhengzhou.cashflow.transaction_edit

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChooseDateSection(
    date: Date,
    onSetNewDate: (Date) -> Unit,
    modifier: Modifier = Modifier,
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.time
    )
    var showDialog by remember {
        mutableStateOf(false)
    }

    // TODO: implement date selection
    Row {
        TextButton(onClick = { showDialog = true }) {

            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null, // TODO: add description
                modifier = modifier.padding(horizontal = 2.dp),
            )
            Text(
                text = DateFormat.format(
                    "EEEE, dd MMM yyyy",
                    date
                ).toString(),
                modifier = modifier.padding(horizontal = 2.dp),
            )
        }
    }
    
    if (showDialog) {

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { 
                TextButton(
                    onClick = {
                        onSetNewDate(Date(datePickerState.selectedDateMillis ?: date.time))
                        showDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                } 
            }
        ) {
            DatePicker(state = datePickerState)
        }

    }


}

