package com.zhengzhou.cashflow.total_balance

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.database.api.complex_data.TransactionAndCategory
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.tools.ui_elements.transaction.SectionTransactionEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionsInCategoryAlertDialog(
    show: Boolean,
    onDismissDialog: (Boolean) -> Unit,
    transactionList: List<TransactionAndCategory>,
    currency: Currency,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    if (show) {
        BasicAlertDialog(onDismissRequest = { onDismissDialog(false) }) {
            Card {
                LazyColumn(
                    modifier = modifier
                ) {
                    if (transactionList.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.no_transactions),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    } else {
                        items(transactionList.size) { position ->
                            val transaction = transactionList[position]
                            SectionTransactionEntry(
                                transaction = transaction.transaction,
                                category = transaction.category,
                                currency = currency,
                                onClickTransaction = {
                                    Screen.TransactionReport.navigate(
                                        transactionUUID = transaction.transaction.id,
                                        navController = navController,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}