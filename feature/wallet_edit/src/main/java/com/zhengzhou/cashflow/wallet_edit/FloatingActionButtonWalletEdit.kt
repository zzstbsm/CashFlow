package com.zhengzhou.cashflow.wallet_edit

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditUiState
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditViewModel

@Composable
internal fun WalletEditFloatingActionButton(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    navController: NavController
) {
    FloatingActionButton(
        onClick = {

            if (walletEditUiState.isErrorWalletNameInUse) {
                EventMessages.sendMessageId(R.string.error_wallet_name_already_in_use)
            } else if (walletEditUiState.isErrorWalletNameNotValid) {
                EventMessages.sendMessageId(R.string.error_wallet_name_not_valid)
            } else {

                val walletEditSaveResults: WalletEditSaveResults = walletEditViewModel.saveWallet()
                EventMessages.sendMessageId(walletEditSaveResults.message)
                val ifSuccess = walletEditSaveResults == WalletEditSaveResults.SUCCESS

                if (ifSuccess) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Screen.WalletOverview.handleKeyWalletUUID,
                            walletEditUiState.wallet.id.toString()
                        )
                    navController.popBackStack(
                        route = Screen.WalletOverview.route,
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