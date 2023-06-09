package com.zhengzhou.cashflow.ui.balance

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.formatCurrency
import com.zhengzhou.cashflow.tools.addDaysFromDate
import com.zhengzhou.cashflow.tools.balanceFlowIn
import com.zhengzhou.cashflow.tools.balanceFlowOut
import com.zhengzhou.cashflow.tools.transactionListFilterPeriod
import java.util.*

@Composable
fun CreditCardSection(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 32.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateString = DateFormat.format(
                    "EEEE, dd MMMM yyyy",
                    Date()
                ).toString()

                Text(
                    text = dateString
                )
                IconButton(onClick = { /*TODO: Navigate in the edit group part */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = stringResource(id = R.string.wallet_edit)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            CardText(
                balanceUiState = balanceUiState,
                balanceViewModel = balanceViewModel,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun CardText(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    modifier: Modifier = Modifier
) {
    // TODO: implement function that refreshes the amount
    val currencyFormatter = balanceViewModel.getCurrencyFormatter()

    val balance = balanceViewModel.walletGroupBalance()
    // TODO: Add feature to change the range
    val numberOfDays = 30
    val transactionListFilteredByDate = transactionListFilterPeriod(
        transactionList = balanceViewModel.getTransactionList(),
        startDate = addDaysFromDate(Date(),- numberOfDays),
        endDate = Date()
    )
    val balanceIn = balanceFlowIn(transactionListFilteredByDate)
    val balanceOut = balanceFlowOut(transactionListFilteredByDate)

    val formattedBalance = formatCurrency(currencyFormatter, balance)
    val formattedIn = formatCurrency(currencyFormatter,balanceIn)
    val formattedOut = formatCurrency(currencyFormatter,balanceOut)

    val wallet = balanceUiState.equivalentWallet

    Text(
        text = wallet.name,
        modifier = modifier
            .padding(vertical = 8.dp),
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = stringResource(R.string.balance,formattedBalance)
    )
    Spacer(modifier = modifier.height(16.dp))
    MonthlyBalance(
        cashIn = formattedIn,
        cashOut = formattedOut
    )

}

@Composable
private fun MonthlyBalance(
    cashIn: String,
    cashOut: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        Text(
            text = stringResource(id = R.string.this_month_balance)
        )
        Row {
            Image(
                painter = painterResource(R.drawable.ic_arrow_up),
                colorFilter = ColorFilter.tint(Color.Green),
                contentDescription = null
            )
            Text(
                text = cashIn,
                color = Color.Green
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(R.drawable.ic_arrow_down),
                colorFilter = ColorFilter.tint(Color.Red),
                contentDescription = null
            )
            Text(
                text = cashOut,
                color = Color.Red
            )
        }
    }
}