package com.zhengzhou.cashflow.ui.walletEdit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Composable
fun WalletEditScreen(
    walletEditOption: WalletEditOption,
    navController: NavController
) {

    val walletEditViewModel: WalletEditViewModel = viewModel {
        WalletEditViewModel(
            navController = navController
        )
    }
    val walletEditUiState by walletEditViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WalletEditTopAppBar(navController = navController)
        },
        content = { innerPadding ->
            WalletEditMainBody(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                innerPadding = innerPadding,
            )
        },
    )
}

@Composable
fun WalletEditMainBody(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    innerPadding: PaddingValues,
) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletEditTopAppBar(
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.add_wallet))
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