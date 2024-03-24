package com.zhengzhou.cashflow.wallet_overview

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
import com.zhengzhou.cashflow.tools.CurrencyFormatter
import com.zhengzhou.cashflow.wallet_overview.view_model.WalletOverviewUiState
import com.zhengzhou.cashflow.wallet_overview.view_model.WalletOverviewViewModel

@Composable
internal fun WalletInfoSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = if (walletOverviewUiState.ifZeroWallet) {
                stringResource(id = R.string.no_wallet)
            } else {
                walletOverviewUiState.wallet.name
            },
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(
                WalletOverviewTestTag.TAG_TEXT_WALLET_NAME
            )
        )
        if (!walletOverviewUiState.ifZeroWallet) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.balance_one_wallet)
                )
                Text(
                    text = CurrencyFormatter.formatCurrency(
                        walletOverviewUiState.wallet.currency,
                        walletOverviewUiState.currentAmountInTheWallet
                    ),
                    modifier = Modifier.testTag(
                        WalletOverviewTestTag.TAG_TEXT_WALLET_AMOUNT
                    )
                )
            }
        }
    }
}