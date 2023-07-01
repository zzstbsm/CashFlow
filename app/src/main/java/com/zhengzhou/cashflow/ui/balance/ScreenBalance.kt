package com.zhengzhou.cashflow.ui.balance

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
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Preview
@Composable
private fun BalanceScreenPreview(){
    BalanceScreen(
        currentScreen = NavigationCurrentScreen.Balance,
        setCurrentScreen = { },
        navController = rememberNavController(),
    )
}

@Composable
fun BalanceScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val balanceViewModel: BalanceViewModel = viewModel {
        BalanceViewModel()
    }
    val balanceUiState by balanceViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Balance.route,
        navController = navController,
    ) {
        setCurrentScreen(NavigationCurrentScreen.Balance)
    }

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController
            )
        },
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                )
            },
            content = { innerPadding ->
                BalanceMainBody(
                    balanceUiState = balanceUiState,
                    balanceViewModel = balanceViewModel,
                    innerPaddingValues = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController,
                )
            },
            floatingActionButton = {
                if (!balanceUiState.isLoading) {
                    BalanceFloatingActionButtons(
                        wallet = balanceUiState.getLastWallet(),
                        transaction = Transaction(),
                        navController = navController,
                    )
                }
            },
        )
    }

}

@Composable
private fun BalanceMainBody(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    innerPaddingValues: PaddingValues,
    navController: NavController,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp, vertical = 4.dp)

    LazyColumn(
        contentPadding = innerPaddingValues
    ) {
        item {
            CreditCardSection(
                balanceUiState = balanceUiState,
                balanceViewModel = balanceViewModel,
                modifier = modifier
            )
        }

        items(balanceUiState.transactionList.size) {position ->
            val transactionCategoryGroup = balanceUiState.transactionList[position]
            TransactionEntry(
                transactionAndCategory = transactionCategoryGroup,
                currencyFormatter = Currency.setCurrencyFormatter(
                    balanceUiState.equivalentWallet.currency.abbreviation
                ),
                onClickTransaction = {
                    Screen.TransactionReport.navigate(
                        transactionUUID = transactionCategoryGroup.transaction.id,
                        navController = navController,
                    )
                },
                modifier = modifier
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BalanceFloatingActionButtons(
    wallet: Wallet,
    transaction: Transaction = Transaction(),
    navController: NavController,
) {
    
    var showDialog by remember{ mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(id = R.string.accessibility_transaction_new),
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
                        Screen.TransactionEdit.navigate(
                            walletUUID = wallet.id,
                            transactionType = TransactionType.Deposit,
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
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
                        Screen.TransactionEdit.navigate(
                            walletUUID = wallet.id,
                            transactionType = TransactionType.Expense,
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
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
                        Screen.TransactionEdit.navigate(
                            walletUUID = wallet.id,
                            transactionType = TransactionType.Move,
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
                        showDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}