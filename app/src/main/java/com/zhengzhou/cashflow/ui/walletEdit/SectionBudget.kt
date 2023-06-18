package com.zhengzhou.cashflow.ui.walletEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.BudgetCategory
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.formatCurrency
import com.zhengzhou.cashflow.data.setCurrencyFormatter
import com.zhengzhou.cashflow.ui.DateSelector
import java.text.NumberFormat

@Composable
fun SectionWalletBudget(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

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
                onCheckedChange = { budgetEnabled ->
                    walletEditViewModel.updateWalletBudgetEnabled(budgetEnabled = budgetEnabled)
                },
            )
        }

        if (walletEditUiState.wallet.budgetEnabled) {



            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            SectionWalletSetBudget(
                walletEditUiState = walletEditUiState,
                walletEditViewModel =  walletEditViewModel,
                modifier = modifier,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            CategoryBudgetSetter(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
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
            dateFormat = "dd/MM/yyyy",
            date = walletEditUiState.budgetPeriod.startDate,
            onSelectDate = { millis ->
                walletEditViewModel.updateWalletBudgetStartDate(millis)
            },
            modifier = Modifier.weight(1f),
            selectableDatesCondition = { utcTimeMillis ->
                utcTimeMillis <= walletEditUiState.budgetPeriod.endDate.time
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        DateSelector(
            label = stringResource(id = R.string.to),
            dateFormat = "dd/MM/yyyy",
            date = walletEditUiState.budgetPeriod.endDate,
            onSelectDate = { millis ->
                walletEditViewModel.updateWalletBudgetEndDate(millis)
            },
            modifier = Modifier.weight(1f),
            selectableDatesCondition = { utcTimeMillis ->
                utcTimeMillis >= walletEditUiState.budgetPeriod.startDate.time
            },
        )
    }
}



@Composable
private fun CategoryBudgetSetter(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier) {

        Text(
            text = stringResource(id = R.string.categories),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
        ){

            items(walletEditUiState.groupCategoryAndBudgetList.size) { pos ->
                val category = walletEditUiState.groupCategoryAndBudgetList[pos].category
                val budgetCategory = walletEditUiState.groupCategoryAndBudgetList[pos].budgetCategory
                CategoryBoxToSet(
                    category = category,
                    budgetCategory = budgetCategory,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}

@Composable
private fun CategoryBoxToSet(
    category: Category,
    budgetCategory: BudgetCategory,
    onClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currencyFormatter: NumberFormat = setCurrencyFormatter(Currency.EUR.abbreviation)
    val formattedBudget = if (budgetCategory.enabled) {
        formatCurrency(currencyFormatter,budgetCategory.maxCategoryAmount)
    } else {
        stringResource(id = R.string.WalletEdit_not_set)
    }

    OutlinedButton(
        onClick = { onClick(category) },
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
            Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = category.idIcon),
                contentDescription = null, // TODO: add content description
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(id = category.name),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
            Text(
                text = formattedBudget,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
        }
    }
}