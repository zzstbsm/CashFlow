package com.zhengzhou.cashflow.wallet_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.tools.ui_elements.transaction.SectionTransactionEntry
import com.zhengzhou.cashflow.wallet_overview.view_model.WalletOverviewUiState

@Composable
internal fun TransactionListSection(
    walletOverviewUiState: WalletOverviewUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(
                id = if (walletOverviewUiState.transactionAndCategoryList.isEmpty()) {
                    R.string.no_transactions
                } else {
                    R.string.recent_transactions
                }
            ),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
        )

        if (walletOverviewUiState.transactionAndCategoryList.isNotEmpty()) {
            walletOverviewUiState.transactionAndCategoryList.forEach { item ->
                val transaction = item.transaction
                val category = item.category
                SectionTransactionEntry(
                    transaction = transaction,
                    category = category,
                    currency = walletOverviewUiState.wallet.currency,
                    onClickTransaction = {
                        Screen.TransactionReport.navigate(
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
                    },
                    modifier = modifier,
                )
            }
            TextButton(
                onClick = {
                    Screen.AllTransactions.navigate(
                        walletUUID = walletOverviewUiState.wallet.id,
                        navController = navController,
                    )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.see_all)
                )
            }
        }
    }
}