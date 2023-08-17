package com.zhengzhou.cashflow.ui.allTransactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Currency
import java.util.UUID

@Composable
fun AllTransactionsScreen(
    walletUUID: UUID,
    categoryUUID: UUID,
    currency: Currency,
    navController: NavController
) {

    val allTransactionsViewModel: AllTransactionsViewModel = viewModel {
        AllTransactionsViewModel(
            walletUUID = walletUUID,
            categoryUUID = categoryUUID,
            currency = currency,
        )
    }
    val allTransactionsUiState by allTransactionsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AllTransactionsTopAppBar(
                onNavigationIconClick = { navController.popBackStack() }
            )
        },
        content = { innerPadding ->
            AllTransactionsMainBody(
                allTransactionsUiState = allTransactionsUiState,
                allTransactionsViewModel = allTransactionsViewModel,
                innerPadding = innerPadding,
                navController = navController,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsTopAppBar(
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.AllTransactions_TopAppBar)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(id = R.string.nav_back)
                )
            }
        }
    )
}

@Composable
fun AllTransactionsMainBody(
    allTransactionsUiState: AllTransactionsUiState,
    allTransactionsViewModel: AllTransactionsViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        LazyColumn{

        }
    }

}