package com.zhengzhou.cashflow.ui.balance

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.tools.balanceFlowIn
import com.zhengzhou.cashflow.tools.balanceFlowOut
import java.util.*

@Composable
fun CreditCardSection(
    balanceUiState: BalanceUiState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(250.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                balance = balanceUiState.getBalance(),
                currency = balanceUiState.equivalentWallet.currency,
                transactionList = balanceUiState.transactionListToShow.map {
                        transactionAndCategory -> transactionAndCategory.transaction
                },
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun CardText(
    balance: Float,
    currency: Currency,
    transactionList: List<Transaction>,
    modifier: Modifier = Modifier
) {
    // TODO: implement function that refreshes the amount
    val currencyFormatter = remember {
        Currency.setCurrencyFormatter(currency.abbreviation)
    }

    val balanceIn = balanceFlowIn(transactionList)
    val balanceOut = balanceFlowOut(transactionList)

    val formattedBalance = Currency.formatCurrency(currencyFormatter, balance)
    val formattedIn = Currency.formatCurrency(currencyFormatter,balanceIn)
    val formattedOut = Currency.formatCurrency(currencyFormatter,balanceOut)

    Text(
        text = stringResource(id = R.string.Balance_wallet_name_currency,stringResource(currency.nameCurrency)),
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