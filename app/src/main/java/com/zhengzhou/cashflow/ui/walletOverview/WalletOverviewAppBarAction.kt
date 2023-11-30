package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import java.util.UUID

@Composable
fun WalletOverviewAppBarAction(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
) {

    var openMenu by remember { mutableStateOf(false) }

    Row {
        if (!walletOverviewUiState.ifZeroWallet) {
            IconButton(
                onClick = {
                    com.zhengzhou.cashflow.navigation.Screen.WalletEdit.navigate(
                        walletID = walletOverviewUiState.wallet.id,
                        navController = navController,
                    )
                },
                modifier = Modifier.testTag(
                    WalletOverviewTestTag.TAG_TOP_APP_BAR_ACTION_EDIT_WALLET
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.WalletOverview_edit_wallet)
                )
            }

            IconButton(
                onClick = { openMenu = true },
                modifier = Modifier.testTag(
                    WalletOverviewTestTag.TAG_TOP_APP_BAR_OPEN_MENU
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = stringResource(R.string.accessibility_menu_action_open),
                )
            }
        }
    }
    DropdownMenu(
        expanded = openMenu,
        onDismissRequest = {
            openMenu = false
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.nav_name_wallet_add))
            },
            onClick = {
                com.zhengzhou.cashflow.navigation.Screen.WalletEdit.navigate(
                    walletID = UUID(0L, 0L),
                    navController = navController,
                )
            },
            modifier = Modifier.testTag(
                WalletOverviewTestTag.TAG_DROP_DOWN_MENU_ADD_WALLET
            )
        )

        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.nav_name_wallet_delete))
            },
            onClick = {
                openMenu = false
                if (walletOverviewViewModel.deleteShownWallet() == WalletOverviewReturnResults.CAN_DELETE_WALLET) {
                    com.zhengzhou.cashflow.tools.EventMessages.sendMessageId(R.string.WalletOverview_wallet_deleted)
                } else {
                    com.zhengzhou.cashflow.tools.EventMessages.sendMessageId(R.string.WalletOverview_cannot_delete_wallet)
                }
            },
            modifier = Modifier.testTag(
                WalletOverviewTestTag.TAG_DROP_DOWN_MENU_DELETE_WALLET
            )
        )
    }
}