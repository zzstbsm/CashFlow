package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R

@Composable
fun WalletInfoSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = if (walletOverviewUiState.ifZeroWallet) {
                stringResource(id = R.string.WalletOverview_no_wallet)
            } else {
                walletOverviewUiState.wallet.name
            },
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(
                WalletOverviewTestTag.TAG_TEXT_WALLET_NAME
            )
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.WalletOverview_balance)
            )
            Text(
                text = walletOverviewViewModel.formatCurrency(walletOverviewUiState.currentAmountInTheWallet),
                modifier = Modifier.testTag(
                    WalletOverviewTestTag.TAG_TEXT_WALLET_AMOUNT
                )
            )
        }
    }
}