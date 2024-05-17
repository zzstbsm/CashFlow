package com.zhengzhou.cashflow.all_transactions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.all_transactions.view_model.AllTransactionsViewModel
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.tools.ui_elements.loading.LoadingLayer
import java.util.UUID

/**
 *
 * I don't remember anymore why I put so many parameters, but here they are
 *
 * @param repository to access the database
 * @param walletUUID UUID of the wallet to open
 * @param navController navigation controller.
 *
 */
@Composable
fun AllTransactionsScreen(
    repository: RepositoryInterface,
    walletUUID: UUID,
    navController: NavController
) {

    val allTransactionsViewModel: AllTransactionsViewModel = viewModel {
        AllTransactionsViewModel(
            repository = repository,
            walletUUID = walletUUID,
        )
    }
    val allTransactionsUiState by allTransactionsViewModel.uiState.collectAsState()

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.AllTransactions.route,
        navController = navController,
        onPopBackStack = { }
    )

    Scaffold(
        topBar = {
            AllTransactionsTopAppBar(
                onNavigationIconClick = { navController.popBackStack() }
            )
        },
        content = { innerPadding ->

            if (allTransactionsUiState.isLoading) {
                LoadingLayer(
                    onDismissRequest = {
                        navController.popBackStack()
                    }
                )
            }

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
                stringResource(id = R.string.TopAppBar)
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
