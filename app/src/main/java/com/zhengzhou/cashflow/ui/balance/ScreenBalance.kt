package com.zhengzhou.cashflow.ui.balance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.BottomOptionCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ui.BottomNavigationBar

@Preview
@Composable
private fun BalanceScreenPreview(){
    BalanceScreen(
        bottomOptionCurrentScreen = BottomOptionCurrentScreen.Balance,
        setBottomOptionCurrentScreen = { },
        navController = rememberNavController(),
    )
}

@Composable
fun BalanceScreen(
    bottomOptionCurrentScreen: BottomOptionCurrentScreen,
    setBottomOptionCurrentScreen: (BottomOptionCurrentScreen) -> Unit,
    navController: NavController
) {

    val balanceViewModel: BalanceViewModel = viewModel {
        BalanceViewModel(
            navController = navController
        )
    }
    val balanceUiState by balanceViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
             BalanceTopAppBar(
         )
        },
        content = { innerPadding ->
            BalanceMainBody(
                balanceUiState = balanceUiState,
                balanceViewModel = balanceViewModel,
                navController = navController,
                modifier = Modifier.padding(paddingValues = innerPadding)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                bottomOptionCurrentScreen = bottomOptionCurrentScreen,
                setBottomOptionCurrentScreen = setBottomOptionCurrentScreen,
                navController = navController,
            )
        },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BalanceTopAppBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.wallet))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    // TODO navigation
                },
                content = {
                    Image(
                        painter  = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(id = R.string.accessibility_menu_navbar),
                    )
                },
                modifier = modifier
            )
        }
    )
}

@Composable
private fun BalanceMainBody(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        CreditCardSection(
            balanceUiState = balanceUiState,
            balanceViewModel = balanceViewModel,
        )
        /*
        TransactionsButtonsSection(
            wallet = uiState.walletToShow,
            navController = navController,
        )

        TransactionListSection(
            transactionListState = transactionListState,
            viewModel = viewModel,
            navController = navController,
            currencyFormatter = currencyFormatter,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        )

         */
    }
}