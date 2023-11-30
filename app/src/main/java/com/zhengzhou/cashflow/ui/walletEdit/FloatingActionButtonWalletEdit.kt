package com.zhengzhou.cashflow.ui.walletEdit

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.EventMessages

@Composable
fun WalletEditFloatingActionButton(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    navController: NavController
) {
    FloatingActionButton(
        onClick = {

            if (walletEditUiState.isErrorWalletNameInUse) {
                EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_already_in_use)
            } else if (walletEditUiState.isErrorWalletNameNotValid) {
                EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_not_valid)
            } else {

                val walletEditSaveResults: WalletEditSaveResults = walletEditViewModel.saveWallet()
                EventMessages.sendMessageId(walletEditSaveResults.message)
                val ifSuccess = walletEditSaveResults == WalletEditSaveResults.SUCCESS

                if (ifSuccess) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            com.zhengzhou.cashflow.navigation.Screen.WalletOverview.handleKeyWalletUUID,
                            walletEditUiState.wallet.id.toString()
                        )
                    navController.popBackStack(
                        route = com.zhengzhou.cashflow.navigation.Screen.WalletOverview.route,
                        inclusive = false
                    )
                }
            }
        },
        modifier = Modifier.testTag(
            WalletEditTestTag.TAG_FLOATING_ACTION_BUTTON
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_save),
            contentDescription = null,
        )
    }
}