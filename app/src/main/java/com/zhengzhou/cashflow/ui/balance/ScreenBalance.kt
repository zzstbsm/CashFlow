package com.zhengzhou.cashflow.ui.balance

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.DateSelector
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar
import java.util.Date
import java.util.UUID

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

        val timeFilterList = TimeFilterForSegmentedButton.values().toList()
        item {
            SelectTimeFilter(
                currentTimeFilter = balanceUiState.timeFilter,
                timeFilterList = timeFilterList,
                onSelectTimeFilter = { timeFilter ->
                    balanceViewModel.setTimeFilter(timeFilter)
                },
                modifier = modifier,
            )
            PeriodSelectorInBalance(
                startDate = balanceUiState.filterStartDate,
                endDate = balanceUiState.filterEndDate,
                onSelectTimePeriod = { startDate, endDate ->
                    balanceViewModel.setTimeFilter(
                        timeFilter = null,
                        startDate = startDate,
                        endDate = endDate,
                    )
                },
                modifier = modifier
            )
        }

        items(balanceUiState.transactionListToShow.size) {position ->
            val transactionCategoryGroup = balanceUiState.transactionListToShow[position]
            TransactionEntry(
                transaction = transactionCategoryGroup.transaction,
                category = transactionCategoryGroup.category,
                currencyFormatter = balanceViewModel.getCurrencyFormatter(),
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
                        if (wallet.id != UUID(0L,0L)) {
                            Screen.TransactionEdit.navigate(
                                walletUUID = wallet.id,
                                transactionType = TransactionType.Deposit,
                                transactionUUID = transaction.id,
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
                                walletUUID = wallet.id,
                                transactionType = TransactionType.Expense,
                                transactionUUID = transaction.id,
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
                                walletUUID = wallet.id,
                                transactionType = TransactionType.Move,
                                transactionUUID = transaction.id,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectTimeFilter(
    currentTimeFilter: TimeFilterForSegmentedButton?,
    timeFilterList: List<TimeFilterForSegmentedButton>,
    onSelectTimeFilter: (TimeFilterForSegmentedButton?) -> Unit,
    modifier: Modifier = Modifier,
) {

    MultiChoiceSegmentedButtonRow(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        timeFilterList.forEachIndexed { index, element ->

            val currentSelected = element == currentTimeFilter
            SegmentedButton(
                shape = SegmentedButtonDefaults.shape(
                    position = index,
                    count = timeFilterList.size,
                ),
                onCheckedChange = {
                    onSelectTimeFilter(element)
                },
                checked = currentSelected
            ) {
                Text(
                    text = stringResource(id = element.textId)
                )
            }
        }
    }
}

@Composable
fun PeriodSelectorInBalance(
    startDate: Date,
    endDate: Date,
    onSelectTimePeriod: (Date,Date) -> Unit = { _, _ -> },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {

        DateSelector(
            label = stringResource(id = R.string.from),
            dateFormat = "dd/MM/yyyy",
            date = startDate,
            onSelectDate = { millis ->
                if (millis != null) {
                    startDate.time = millis
                }
                onSelectTimePeriod(startDate, endDate)
            },
            modifier = Modifier
                .padding(4.dp)
                .weight(1f),
            selectableDatesCondition = { utcTimeMillis ->
                utcTimeMillis <= endDate.time
            },
        )

        DateSelector(
            label = stringResource(id = R.string.from),
            dateFormat = "dd/MM/yyyy",
            date = endDate,
            onSelectDate = { millis ->
                if (millis != null) {
                    endDate.time = millis
                }
                onSelectTimePeriod(startDate, endDate)
            },
            modifier = Modifier
                .padding(4.dp)
                .weight(1f),
            selectableDatesCondition = { utcTimeMillis ->
                utcTimeMillis >= startDate.time
            },
        )
    }
}