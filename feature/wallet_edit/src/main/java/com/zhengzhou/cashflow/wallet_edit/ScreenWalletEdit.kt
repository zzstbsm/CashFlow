package com.zhengzhou.cashflow.wallet_edit

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditViewModel
import java.util.UUID


@Composable
fun WalletEditScreen(
    repository: RepositoryInterface,
    walletUUID: UUID,
    navController: NavController
) {

    val walletEditOption = WalletEditOption.chooseOption(walletUUID)

    val walletEditViewModel: WalletEditViewModel = viewModel {
        WalletEditViewModel(
            repository = repository,
            walletUUID = walletUUID,
        )
    }
    val walletEditUiState by walletEditViewModel.uiState.collectAsState()

    var keepLoadingScreen by remember {
        mutableStateOf(true)
    }

    if (walletEditUiState.isLoading && keepLoadingScreen) {
        Dialog(onDismissRequest = {
            keepLoadingScreen = false
            navController.popBackStack()
        }) {
            CircularProgressIndicator()
        }
    }

    Scaffold(
        topBar = {
            WalletEditTopAppBar(
                walletEditOption = walletEditOption,
                navController = navController,
            )
        },
        content = { innerPadding ->
            WalletEditMainBody(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                innerPadding = innerPadding,
            )
        },
        floatingActionButton = {
            WalletEditFloatingActionButton(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                navController = navController,
            )
        }
    )
}