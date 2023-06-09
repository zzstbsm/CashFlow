package com.zhengzhou.cashflow.ui.balance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
             BalanceTopAppBar()
        },
        content = { innerPadding ->
            BalanceMainBody(
                balanceUiState = balanceUiState,
                balanceViewModel = balanceViewModel,
                innerPaddingValues = innerPadding,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                bottomOptionCurrentScreen = bottomOptionCurrentScreen,
                setBottomOptionCurrentScreen = setBottomOptionCurrentScreen,
                navController = navController,
            )
        },
        floatingActionButton = {
            BalanceFloatingActionButtons()
        }
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
    innerPaddingValues: PaddingValues,
) {
    LazyColumn(
        contentPadding = innerPaddingValues
    ) {
        item {
            CreditCardSection(
                balanceUiState = balanceUiState,
                balanceViewModel = balanceViewModel,
            )
        }

        items(balanceUiState.balanceGroup.transactionList.size) {position ->
            val transaction = balanceUiState.balanceGroup.transactionList[position]
            TransactionEntry(
                transaction = transaction,
                currencyFormatter = balanceUiState.balanceGroup.currencyFormatter,
                balanceViewModel = balanceViewModel,
                onClickTransaction = { },
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BalanceFloatingActionButtons() {
    
    var showDialog by remember{ mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "New transaction", // TODO: Put in strings 
        )
    }
    
    if (showDialog) {
                
        AlertDialog(onDismissRequest = { showDialog = false }) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ExtendedFloatingActionButton(
                    text = { 
                        Text(text = stringResource(id = R.string.new_deposit))
                    },
                    icon = { 
                       Icon(
                           painter = painterResource(id = R.drawable.ic_add),
                           contentDescription = stringResource(id = R.string.new_deposit),
                       )
                    },
                    onClick = {
                        /*TODO*/
                        showDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExtendedFloatingActionButton(
                    text = {
                        Text(text = stringResource(id = R.string.new_expense))
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = stringResource(id = R.string.new_expense),
                        )
                    },
                    onClick = {
                        /*TODO*/
                        showDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExtendedFloatingActionButton(
                    text = {
                        Text(text = stringResource(id = R.string.new_move))
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_transfer),
                            contentDescription = stringResource(id = R.string.new_move),
                        )
                    },
                    onClick = {
                        /*TODO*/
                        showDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}