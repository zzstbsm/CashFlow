package com.zhengzhou.cashflow.wallet_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditUiState
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditViewModel

@Composable
internal fun BudgetSection(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier
) {

    val wallet = walletEditUiState.wallet

    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(text = "Ao")
            Switch(
                checked = wallet.budgetEnabled,
                onCheckedChange = {

                }
            )
        }

        if (wallet.budgetEnabled) {
            BudgetSettings()
        }
    }
}

@Composable
private fun BudgetSettings(
    modifier: Modifier = Modifier,
) {
    Text(text = "Test")
}