package com.zhengzhou.cashflow.ui.balance

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
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.SectionTransactionEntry
import com.zhengzhou.cashflow.dataForUi.TransactionAndCategory
import com.zhengzhou.cashflow.navigation.Screen
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsInCategoryAlertDialog(
    show: Boolean,
    onDismissDialog: (Boolean) -> Unit,
    transactionList: List<TransactionAndCategory>,
    currencyFormatter: NumberFormat,
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
                                text = stringResource(id = R.string.Balance_no_transactions),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    } else {
                        items(transactionList.size) { position ->
                            val transaction = transactionList[position]
                            SectionTransactionEntry(
                                transaction = transaction.transaction,
                                category = transaction.category,
                                currencyFormatter = currencyFormatter,
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