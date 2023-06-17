package com.zhengzhou.cashflow.ui.walletEdit

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import java.util.Date


@Composable
fun SectionWalletBudget(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    var switch by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.WalletEdit_if_budget_on)
            )

            Switch(
                checked = walletEditUiState.wallet.budgetEnabled,
                onCheckedChange = {
                    walletEditViewModel.updateWalletBudgetEnabled(it)
                },
            )
        }

        if (walletEditUiState.wallet.budgetEnabled) {
            SectionWalletSetBudget(
                walletEditUiState = walletEditUiState,
                walletEditViewModel =  walletEditViewModel,
                modifier = modifier,
            )

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SectionWalletSetBudget(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {

        DateSelector(
            label = stringResource(id = R.string.from),
            date = walletEditUiState.budgetPeriod.startDate,
            onSelectDate = { millis ->
                walletEditViewModel.updateWalletBudgetStartDate(millis)
            },
            modifier = Modifier.weight(1f),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= walletEditUiState.budgetPeriod.endDate.time
                }
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        DateSelector(
            label = stringResource(id = R.string.to),
            date = walletEditUiState.budgetPeriod.endDate,
            onSelectDate = { millis ->
                walletEditViewModel.updateWalletBudgetEndDate(millis)
            },
            modifier = Modifier.weight(1f),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= walletEditUiState.budgetPeriod.startDate.time
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelector(
    label: String,
    date: Date,
    onSelectDate: (Long?) -> Unit,
    selectableDates: SelectableDates,
    modifier: Modifier = Modifier,
) {
    var showDatePickerState by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.time,
        selectableDates = selectableDates,
    )

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = DateFormat.format(
            //"EEEE, dd MMMM yyyy",
            "dd/MM/yy",
            date
        ).toString(),
        onValueChange = { },
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    showDatePickerState = true
                    focusManager.clearFocus()
                }
            }
        ,
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

@Composable
private fun CategoryBudgetSetter(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}

@Composable
private fun CategoryBoxToSet(

) {

}