package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.navigation.Screen
import java.util.UUID

@Composable
fun WalletOverviewFloatingActionButton(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
) {

    val spacerSpaceWidthModifier: Modifier = Modifier.width(8.dp)

    val textId: Int
    val iconId: Int
    val onClick: () -> Unit

    if (walletOverviewUiState.ifZeroWallet) {
        textId = R.string.WalletOverview_add_wallet
        iconId = R.drawable.ic_add
        onClick = {
            Screen.WalletEdit.navigate(
                walletID = UUID(0L, 0L),
                navController = navController,
            )
        }
    } else {
        textId = R.string.WalletOverview_select_wallet
        iconId = R.drawable.ic_wallet
        onClick = {
            walletOverviewViewModel.updateWalletList()
            walletOverviewViewModel.showSelectWalletDialog(true)
        }
    }

    ExtendedFloatingActionButton(
        onClick = {
            onClick()
        },
        modifier = Modifier.testTag(
            WalletOverviewTestTag.TAG_FLOATING_ACTION_BUTTON
        )
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = textId),
        )
        Spacer(modifier = spacerSpaceWidthModifier)
        Text(
            text = stringResource(id = textId)
        )
    }
}