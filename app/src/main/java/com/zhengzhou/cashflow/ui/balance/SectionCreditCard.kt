package com.zhengzhou.cashflow.ui.balance

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.zhengzhou.cashflow.tools.CashFlowTools
import java.text.NumberFormat
import java.util.*

@Composable
fun CreditCardSection(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    modifier: Modifier = Modifier,
) {
    var showCurrencySelectorDialog by remember { mutableStateOf(false) }

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
                IconButton(onClick = { showCurrencySelectorDialog = true }) {
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
                currencyFormatter = balanceViewModel.getCurrencyFormatter(),
                transactionList = balanceUiState.transactionListToShow.map {
                    transactionAndCategory -> transactionAndCategory.transaction
                },
                modifier = Modifier,
            )
        }
    }

    SelectGroupCurrency(
        toShow = showCurrencySelectorDialog,
        currencyList = balanceUiState.currencyList,
        onDismissDialog = { showCurrencySelectorDialog = false },
        onSelectCurrency = { currency ->
            balanceViewModel.setWalletListByCurrency(currency)
            showCurrencySelectorDialog = false
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectGroupCurrency(
    toShow: Boolean,
    currencyList: List<Currency>,
    onDismissDialog: () -> Unit,
    onSelectCurrency: (Currency) -> Unit,
) {

    if (toShow) {
        AlertDialog(onDismissRequest = onDismissDialog) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                Text(
                    text = stringResource(id = R.string.Balance_choose_currency),
                    modifier = Modifier
                        .padding(8.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(currencyList.size) { pos ->
                        val currency = currencyList[pos]

                        Card(
                            onClick = { onSelectCurrency(currency) },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                Text(text = currency.iconEmojiUnicode)
                                Spacer(modifier = Modifier
                                    .width(16.dp))
                                Text(text = stringResource(currency.nameCurrency))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardText(
    balance: Float,
    currency: Currency,
    currencyFormatter: NumberFormat,
    transactionList: List<Transaction>,
    modifier: Modifier = Modifier
) {

    val balanceIn = CashFlowTools.balanceFlowIn(transactionList)
    val balanceOut = CashFlowTools.balanceFlowOut(transactionList)

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
            text = stringResource(id = R.string.Balance_selected_period_balance)
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