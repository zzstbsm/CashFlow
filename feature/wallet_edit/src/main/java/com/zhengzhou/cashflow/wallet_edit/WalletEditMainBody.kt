package com.zhengzhou.cashflow.wallet_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditUiState
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditViewModel

@Composable
internal fun WalletEditMainBody(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    innerPadding: PaddingValues,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)

    Column(
        modifier = Modifier.padding(innerPadding),
    ) {

        WalletDetailsSection(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier,
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        BudgetSection(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier,
        )

    }

}