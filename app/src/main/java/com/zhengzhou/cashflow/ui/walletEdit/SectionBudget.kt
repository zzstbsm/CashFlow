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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.zhengzhou.cashflow.tools.mapIconsFromName
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
                enabled = false, // TODO: to remove after start planning to implement the budget
            )
        }

        Text(
            text = "Budget feature not completed",
            modifier = modifier
        )

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
                    onClick = { category, budgetCategory ->
                        // TODO: to implement set budget per category
                    },
                    currency = walletEditUiState.wallet.currency,
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
    onClick: (Category,BudgetCategory) -> Unit,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val currencyFormatter: NumberFormat = Currency.setCurrencyFormatter(currency.abbreviation)
    val formattedBudget = if (budgetCategory.enabled) {
        Currency.formatCurrency(currencyFormatter,budgetCategory.maxCategoryAmount)
    } else {
        stringResource(id = R.string.WalletEdit_not_set)
    }

    OutlinedButton(
        onClick = { onClick(category,budgetCategory) },
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
            Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = mapIconsFromName[category.iconName]!!),
                contentDescription = null, // TODO: add content description
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = category.name,
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