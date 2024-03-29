package com.zhengzhou.cashflow.total_balance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.tools.EventMessages
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceFloatingActionButtons(
    wallet: Wallet,
    transaction: Transaction = Transaction.newEmpty(),
    navController: NavController,
) {

    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(id = R.string.accessibility_transaction_new),
        )
    }

    if (showDialog) {

        BasicAlertDialog(onDismissRequest = { showDialog = false }) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TransactionType.getAllActive().forEach { transactionType ->
                    ExtendedFloatingActionButton(
                        text = {
                            Text(text = stringResource(id = transactionType.newText))
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = transactionType.iconId),
                                contentDescription = stringResource(id = transactionType.newText),
                            )
                        },
                        onClick = {
                            if (wallet.id != UUID(0L, 0L)) {
                                Screen.TransactionEdit.navigate(
                                    transactionType = transactionType,
                                    transactionUUID = transaction.id,
                                    currency = wallet.currency,
                                    isBlueprint = false,
                                    editBlueprint = false,
                                    navController = navController,
                                )
                            } else {
                                EventMessages.sendMessageId(R.string.no_wallet)
                            }
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}