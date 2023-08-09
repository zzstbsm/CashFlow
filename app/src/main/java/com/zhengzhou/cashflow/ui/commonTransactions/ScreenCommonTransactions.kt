package com.zhengzhou.cashflow.ui.commonTransactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionFullForUI
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar
import java.util.UUID

@Composable
fun CommonTransactionsScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val commonTransactionsViewModel: CommonTransactionsViewModel = viewModel {
        CommonTransactionsViewModel()
    }
    val commonTransactionsUiState by commonTransactionsViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.CommonTransactions.route,
        navController = navController,
    ) {
        setCurrentScreen(NavigationCurrentScreen.CommonTransactions)
        commonTransactionsViewModel.reloadScreen()
    }

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController,
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
                    actions = {
                        SectionTopAppBar(
                            currentScreen = currentScreen,
                            drawerState = drawerState,
                        )
                    }
                )
            },
            content = { innerPadding ->
                CommonTransactionsMainBody(
                    commonTransactionsUiState = commonTransactionsUiState,
                    commonTransactionsViewModel = commonTransactionsViewModel,
                    innerPadding = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController
                )
            },
            floatingActionButton = {
                CommonTransactionFloatingActionButtons(
                    wallet = commonTransactionsUiState.walletList.maxByOrNull { wallet ->
                        wallet.lastAccess
                    } ?: Wallet.newEmpty(),
                    transaction = Transaction.newEmpty(),
                    navController = navController,
                )
            }
        )
    }
}

@Composable
private fun CommonTransactionsMainBody(
    commonTransactionsUiState: CommonTransactionsUiState,
    commonTransactionsViewModel: CommonTransactionsViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {

    if (commonTransactionsUiState.transactionFullForUIList.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.CommonTransactions_no_model)
            )
        }
    } else {
        CommonTransactionsNonEmptyList(
            commonTransactionsUiState = commonTransactionsUiState,
            commonTransactionsViewModel = commonTransactionsViewModel,
            innerPadding = innerPadding,
            navController = navController,
        )
    }
}

@Composable
private fun CommonTransactionsNonEmptyList(
    commonTransactionsUiState: CommonTransactionsUiState,
    commonTransactionsViewModel: CommonTransactionsViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        items(commonTransactionsUiState.transactionFullForUIList.size) {position ->
            val transactionFullForUI = commonTransactionsUiState.transactionFullForUIList[position]
            SingleTransaction(
                transactionFullForUI = transactionFullForUI,
                walletList = commonTransactionsUiState.walletList,
                onCreateTransaction = {
                    commonTransactionsViewModel.addTransaction(transactionFullForUI)
                },
                onEditTransaction = {
                    val transaction = transactionFullForUI.transaction
                    Screen.TransactionEdit.navigate(
                        transactionType = transaction.transactionType,
                        transactionUUID = transaction.id,
                        currency = transactionFullForUI.wallet.currency,
                        isBlueprint = true,
                        editBlueprint = false,
                        navController = navController,
                    )
                },
                onEditTransactionModel = {
                    val transaction = transactionFullForUI.transaction
                    Screen.TransactionEdit.navigate(
                        transactionType = transaction.transactionType,
                        transactionUUID = transaction.id,
                        currency = transactionFullForUI.wallet.currency,
                        isBlueprint = true,
                        editBlueprint = true,
                        navController = navController,
                    )
                },
                onDeleteTransaction = {
                    commonTransactionsViewModel.deleteTransaction(transactionFullForUI)
                },
                onChangeWallet = { wallet ->
                    commonTransactionsViewModel.changeWallet(position, wallet)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSingleTransaction() {
    val transactionFullForUI = TransactionFullForUI(
        transaction = Transaction.newEmpty().copy(
            description = "Description/Title",
            amount = 100f,
            transactionType = TransactionType.Expense,
        ),
        wallet = Wallet.newEmpty().copy(
            name = "Preview wallet",
            currency = Currency.EUR,
        ),
        category = Category.newEmpty().copy(
            name = "Category",
            iconName = IconsMappedForDB.HOME
        ),
        tagList = listOf(
            Tag(
                name = "Gardaland"
            ),
            Tag(
                name = "Sky"
            ),
            Tag(
                name = "Glass"
            )
        )
    )

    val walletList = listOf(
        Wallet.newEmpty().copy(
            name = "Preview wallet"
        ),
        Wallet.newEmpty().copy(
            name = "Second Wallet"
        )
    )

    SingleTransaction(
        transactionFullForUI = transactionFullForUI,
        walletList = walletList,
        onCreateTransaction = { },
        onEditTransaction = { },
        onEditTransactionModel = { },
        onDeleteTransaction = { },
        onChangeWallet = { _ ->  }
    )
}

@Composable
private fun SingleTransaction(
    transactionFullForUI: TransactionFullForUI,
    walletList: List<Wallet>,
    onCreateTransaction: () -> Unit,
    onEditTransaction: () -> Unit,
    onEditTransactionModel: () -> Unit,
    onDeleteTransaction: () -> Unit,
    onChangeWallet: (Wallet) -> Unit,
) {

    var ifOpen by remember {
        mutableStateOf(false)
    }

    val transaction = transactionFullForUI.transaction
    val wallet = transactionFullForUI.wallet
    val tagList = transactionFullForUI.tagList
    val category = transactionFullForUI.category

    val currencyFormatter = Currency.setCurrencyFormatter(wallet.currency.abbreviation)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            SingleTransactionVisibleSection(
                transaction = transaction,
                category = category,
                currencyFormatter = currencyFormatter,
                onCreateTransaction = onCreateTransaction,
                onEditTransaction = onEditTransaction,
                ifTransactionOpen = ifOpen,
                onOpenTransaction = { ifOpen = it },
            )

            if (ifOpen) {
                SingleTransactionHiddenSection(
                    wallet = wallet,
                    walletList = walletList,
                    tagList = tagList,
                    onChangeWallet = onChangeWallet,
                    onEditTransactionModel = onEditTransactionModel,
                    onDeleteTransaction = onDeleteTransaction,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommonTransactionFloatingActionButtons(
    wallet: Wallet,
    transaction: Transaction,
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
                        if (wallet.id != UUID(0L,0L)) {
                            Screen.TransactionEdit.navigate(
                                transactionType = TransactionType.Deposit,
                                transactionUUID = transaction.id,
                                currency = wallet.currency,
                                isBlueprint = true,
                                editBlueprint = true,
                                navController = navController,
                            )
                        } else {
                            EventMessages.sendMessageId(R.string.Balance_no_wallet)
                        }
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
                        if (wallet.id != UUID(0L,0L)) {
                            Screen.TransactionEdit.navigate(
                                transactionType = TransactionType.Expense,
                                transactionUUID = transaction.id,
                                currency = wallet.currency,
                                isBlueprint = true,
                                editBlueprint = true,
                                navController = navController,
                            )
                        } else {
                            EventMessages.sendMessageId(R.string.Balance_no_wallet)
                        }
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
                        if (wallet.id != UUID(0L,0L)) {
                            Screen.TransactionEdit.navigate(
                                transactionType = TransactionType.Move,
                                transactionUUID = transaction.id,
                                currency = wallet.currency,
                                isBlueprint = true,
                                editBlueprint = true,
                                navController = navController,
                            )
                        } else {
                            EventMessages.sendMessageId(R.string.Balance_no_wallet)
                        }
                        showDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}