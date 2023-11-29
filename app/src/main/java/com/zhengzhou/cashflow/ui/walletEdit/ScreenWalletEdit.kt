package com.zhengzhou.cashflow.ui.walletEdit

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import java.util.UUID

private enum class WalletEditOption(
    @StringRes
    val title: Int,
){
    ADD(
        title = R.string.nav_name_wallet_add
    ),
    EDIT(
        title = R.string.nav_name_wallet_edit
    );

    companion object {
        fun chooseOption(id: UUID) : WalletEditOption{
            return if(id == UUID(0L,0L)) {
                ADD
            } else {
                EDIT
            }
        }
    }

}

@Composable
fun WalletEditScreen(
    walletUUID: UUID,
    navController: NavController
) {

    val walletEditOption = WalletEditOption.chooseOption(walletUUID)

    val walletEditViewModel: WalletEditViewModel = viewModel {
        WalletEditViewModel(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletEditTopAppBar(
    walletEditOption: WalletEditOption,
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = walletEditOption.title))
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(id = R.string.nav_back)
                )
            }
        }
    )
}

@Composable
fun WalletEditMainBody(
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