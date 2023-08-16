package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.SectionTransactionEntry
import com.zhengzhou.cashflow.navigation.Screen

@Composable
fun TransactionListSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(
                id = if (walletOverviewUiState.transactionAndCategoryList.isEmpty()) {
                    R.string.WalletOverview_no_transactions
                } else {
                    R.string.WalletOverview_recent_transactions
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
                    currencyFormatter = walletOverviewViewModel.currencyFormatter,
                    onClickTransaction = {
                        Screen.TransactionReport.navigate(
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
                    },
                    modifier = modifier,
                )
            }
        }
    }
}